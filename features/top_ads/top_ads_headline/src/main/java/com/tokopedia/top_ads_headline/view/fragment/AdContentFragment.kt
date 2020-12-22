package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.top_ads_headline.data.CpmModelMapper
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.activity.TopAdsProductListActivity
import com.tokopedia.top_ads_headline.view.adapter.SINGLE_SELECTION
import com.tokopedia.top_ads_headline.view.sheet.PromotionalMessageBottomSheet
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.view.TopAdsProductImagePreviewWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ad_content_fragment.*
import javax.inject.Inject

private const val SELECT_PRODUCT_REQUEST_CODE = 1001
private const val MAX_PRODUCT_PREVIEW = 3
private const val MIN_PROMOTIONAL_MSG_COUNT = 20

class AdContentFragment : BaseHeadlineStepperFragment<HeadlineAdStepperModel>(), TopAdsProductImagePreviewWidget.TopAdsImagePreviewClick {

    companion object {
        fun newInstance() = AdContentFragment()
    }

    private var sharedEditHeadlineViewModel: SharedEditHeadlineViewModel? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var cpmModelMapper: CpmModelMapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ad_content_fragment, container, false)
    }

    override fun getScreenName(): String {
        return AdContentFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (stepperListener == null) {
            setUpObservers()
        }
    }

    private fun setUpObservers() {
        activity?.let {
            sharedEditHeadlineViewModel = ViewModelProvider(it, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
        }
        sharedEditHeadlineViewModel?.getEditHeadlineAdLiveData()?.observe(viewLifecycleOwner, Observer {
            stepperModel = it
            onStepperModelChange()
        })
    }

    private fun onStepperModelChange() {
        if (stepperModel?.selectedTopAdsProducts?.isNotEmpty() == true) {
            onProductsSelectionChange()
        }
        promotionalMessageInputText.textFieldInput.setText(stepperModel?.slogan
                ?: getString(R.string.topads_headline_promotional_dummy_message))
        showTopAdsBannerPreview()
    }

    override fun populateView() {
        setUpSelectedText()
        productImagePreviewWidget.setTopAdsImagePreviewClick(this)
        promotionalMessageInputText.textFieldInput.setText(stepperModel?.slogan
                ?: getString(R.string.topads_headline_promotional_dummy_message))
        promotionalMessageInputText.textFieldInput.isFocusable = false
        promotionalMessageInputText.textFieldInput.setOnClickListener {
            openPromotionalMessageBottomSheet()
        }
        if (stepperListener == null) {
            btnSubmit.hide()
        } else {
            onStepperModelChange()
            btnSubmit.setOnClickListener {
                onClickSubmit()
            }
            btnSubmit.show()
        }
    }

    private fun onClickSubmit() {
        stepperModel?.selectedTopAdsProducts = getSelectedProducts()
        when {
            ifLessProductSelected() -> {
                productPickerErrorText.show()
                view?.let { it1 ->
                    Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_60)
                    Toaster.build(it1, getString(R.string.topads_headline_submit_ad_detail_error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
            stepperModel?.selectedTopAdsProducts?.size ?: 0 > MAX_PRODUCT_SELECTION -> {
                view?.let {
                    Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_60)
                    Toaster.build(it, getString(R.string.topads_headline_over_product_selection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
            else -> {
                gotoNextPage()
            }
        }
    }

    private fun ifLessProductSelected(): Boolean {
        if (stepperModel?.selectedTopAdsProducts?.isEmpty() == true) {
            return true
        } else {
            stepperModel?.selectedTopAdsProductMap?.forEach { (_, arrayList) ->
                if (arrayList.size == SINGLE_SELECTION) {
                    return true
                }
            }
            return false
        }
    }

    private fun showTopAdsBannerPreview() {
        val cpmModel = stepperModel?.selectedTopAdsProducts?.take(MAX_PRODUCT_PREVIEW)?.let {
            cpmModelMapper.getCpmModelResponse(it, stepperModel?.slogan
                    ?: "")
        }
        if (cpmModel != null) {
            stepperModel?.cpmModel = cpmModel
        }
        topAdsBannerView.displayAdsWithProductShimmer(stepperModel?.cpmModel, true)
    }

    private fun openPromotionalMessageBottomSheet() {
        val promotionalMessageBottomSheet = PromotionalMessageBottomSheet.newInstance(userSession.shopName,
                stepperModel?.slogan ?: "", this::onPromotionalBottomSheetDismiss)
        promotionalMessageBottomSheet.show(childFragmentManager, "")
    }

    private fun onPromotionalBottomSheetDismiss(promotionalMessage: String) {
        promotionalMessageInputText.textFieldInput.setText(promotionalMessage)
        stepperModel?.let {
            it.slogan = promotionalMessage
            it.cpmModel.data[0].cpm.cpmShop.slogan = promotionalMessage
            topAdsBannerView.displayAds(stepperModel?.cpmModel)
        }
        btnSubmit.isEnabled = promotionalMessage.length >= MIN_PROMOTIONAL_MSG_COUNT
    }

    private fun setUpSelectedText() {
        contentSelectedText.text = getString(R.string.topads_headline_product_selected, stepperModel?.selectedTopAdsProducts?.size.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            stepperModel?.selectedTopAdsProductMap = data?.getSerializableExtra(SELECTED_PRODUCT_LIST) as? HashMap<Category, ArrayList<TopAdsProductModel>>
                    ?: HashMap()
            stepperModel?.selectedTopAdsProducts = getSelectedProducts()
            if (data?.getBooleanExtra(IS_EDITED, false) == true) {
                onProductsSelectionChange()
            }
            if (stepperModel?.selectedTopAdsProducts?.isNotEmpty() == true) {
                productPickerErrorText.hide()
            }
        }
    }

    private fun onProductsSelectionChange() {
        val imageList = ArrayList<String>()
        stepperModel?.selectedTopAdsProducts?.forEach {
            imageList.add(it.productImage)
        }
        if (stepperModel?.selectedTopAdsProducts?.size ?: 0 >= MAX_PRODUCT_PREVIEW - 1) {
            showTopAdsBannerPreview()
        }
        productImagePreviewWidget.setSelectedProductList(imageList)
        setUpSelectedText()
    }

    override fun onDeletePreview(position: Int) {
        val product = stepperModel?.selectedTopAdsProducts?.getOrNull(position)
        stepperModel?.selectedTopAdsProductMap?.forEach { (_, arrayList) ->
            if (arrayList.contains(product)) {
                arrayList.remove(product)
            }
        }
        stepperModel?.selectedTopAdsProducts?.removeAt(position)
        setUpSelectedText()
    }

    override fun onClickPreview(position: Int) {
        if (position == 0) {
            val intent = Intent(activity, TopAdsProductListActivity::class.java)
            intent.putExtra(SELECTED_PRODUCT_LIST, stepperModel?.selectedTopAdsProductMap)
            startActivityForResult(intent, SELECT_PRODUCT_REQUEST_CODE)
        }
    }

    private fun getSelectedProducts(): ArrayList<TopAdsProductModel> {
        val result = ArrayList<TopAdsProductModel>()
        stepperModel?.selectedTopAdsProductMap?.forEach { (_, value) ->
            if (value.size > SINGLE_SELECTION) {
                result.addAll(value)
            }
        }
        return result
    }

    override fun gotoNextPage() {
        stepperModel?.slogan = promotionalMessageInputText.textFieldInput.text.toString()
        stepperModel?.selectedProductIds = getAdItems()
        stepperModel?.adOperations = getAdOperations()
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_content_fragment_label))
        }
    }

    private fun getAdOperations(): MutableList<TopAdsManageHeadlineInput.Operation.Group.AdOperation> {
        return mutableListOf(TopAdsManageHeadlineInput.Operation.Group.AdOperation(
                action = ParamObject.ACTION_CREATE,
                ad = TopAdsManageHeadlineInput.Operation.Group.AdOperation.Ad(
                        id = "0",
                        title = stepperModel?.groupName ?: "",
                        slogan = stepperModel?.slogan
                                ?: "",
                        productIDs = ArrayList<String>().apply {
                            stepperModel?.selectedProductIds?.forEach {
                                add(it.toString())
                            }
                        }
                )
        ))
    }

    private fun getAdItems(): MutableList<Int> {
        return stepperModel?.selectedTopAdsProducts?.map {
            it.productID
        }?.toMutableList() ?: mutableListOf()
    }
}