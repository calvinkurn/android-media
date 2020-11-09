package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.activity.TopAdsProductListActivity
import com.tokopedia.top_ads_headline.view.sheet.PromotionalMessageBottomSheet
import com.tokopedia.top_ads_headline.view.viewmodel.AdContentViewModel
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.view.TopAdsProductImagePreviewWidget
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ad_content_fragment.*
import javax.inject.Inject

private const val SELECT_PRODUCT_REQUEST_CODE = 1001

class AdContentFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>(), TopAdsProductImagePreviewWidget.TopAdsImagePreviewClick {

    private var selectedTopAdsProducts = ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>()

    companion object {
        fun newInstance() = AdContentFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: AdContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdContentViewModel::class.java)
    }

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

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateHeadlineAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperModel?.selectedProductIds = getAdItems()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun getAdItems(): MutableList<Int> {
        return selectedTopAdsProducts.map {
            it.productID
        }.toMutableList()
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_content_fragment_label))
        }
    }

    override fun populateView() {
        setUpSelectedText()
        productImagePreviewWidget.setTopAdsImagePreviewClick(this)
        if (selectedTopAdsProducts.isNotEmpty()) {
            onProductsSelectionChange()
        }
        promotionalMessageInputText.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                openPromotionalMessageBottomSheet()
            }
        }
        promotionalMessageInputText.textFieldInput.setOnClickListener {
            openPromotionalMessageBottomSheet()
        }
        btnSubmit.setOnClickListener {
            if (selectedTopAdsProducts.isEmpty()) {
                Toaster.build(it, getString(R.string.topads_headline_submit_ad_detail_error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
            } else {
                gotoNextPage()
            }
        }
        viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), "", "", "", "", 3, 0, this::onSuccess, this::onError)
    }

    private fun onSuccess(cpmModel: CpmModel) {
        topAdsBannerView.displayAds(cpmModel)
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun openPromotionalMessageBottomSheet() {
        val promotionalMessageBottomSheet = PromotionalMessageBottomSheet.newInstance(userSession.shopName,
                this::onPromotionalBottomSheetDismiss)
        promotionalMessageBottomSheet.show(fragmentManager!!, "")
    }

    private fun onPromotionalBottomSheetDismiss(promotionalMessage: String) {
        promotionalMessageInputText.textFieldInput.setText(promotionalMessage)
    }

    private fun setUpSelectedText() {
        contentSelectedText.text = getString(R.string.topads_headline_product_selected, selectedTopAdsProducts.size.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedTopAdsProducts = data?.getParcelableArrayListExtra<ResponseProductList.Result.TopadsGetListProduct.Data>(SELECTED_PRODUCT_LIST) as ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>
            if (data.getBooleanExtra(IS_EDITED, false)) {
                onProductsSelectionChange()
            }
        }
    }

    private fun onProductsSelectionChange() {
        val imageList = ArrayList<String>()
        selectedTopAdsProducts.forEach {
            imageList.add(it.productImage)
        }
        productImagePreviewWidget.setSelectedProductList(imageList)
        setUpSelectedText()
    }

    override fun onDeletePreview(position: Int) {
        selectedTopAdsProducts.removeAt(position)
        setUpSelectedText()
    }

    override fun onClickPreview(position: Int) {
        if (position == 0) {
            val intent = Intent(activity, TopAdsProductListActivity::class.java)
            intent.putExtra(SELECTED_PRODUCT_LIST, selectedTopAdsProducts)
            startActivityForResult(intent, SELECT_PRODUCT_REQUEST_CODE)
        }
    }
}