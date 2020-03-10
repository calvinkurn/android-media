package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.feature.tokopointstnc.TokoPointsTncBottomsheet
import com.tokopedia.purchase_platform.features.promo.data.request.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.purchase_platform.features.promo.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import com.tokopedia.purchase_platform.features.promo.presentation.viewmodel.PromoCheckoutViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.fragment_promo_checkout_marketplace.*
import java.net.UnknownHostException
import javax.inject.Inject

class PromoCheckoutFragment : BaseListFragment<Visitable<*>, PromoCheckoutAdapterTypeFactory>(),
        PromoCheckoutActionListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var itemDecorator: PromoDecoration

    // Use single recycler view to prevent NPE cuased by nested recyclerview
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter

    private var toolbar: ToolbarPromoCheckout? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PromoCheckoutViewModel::class.java]
    }

    companion object {
        const val REQUEST_CODE_PHONE_VERIFICATION = 9999
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        fun createInstance(promoRequest: PromoRequest): PromoCheckoutFragment {
            return PromoCheckoutFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGS_PROMO_REQUEST, promoRequest)
                }
            }
        }
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerPromoCheckoutMarketplaceComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_promo_checkout_marketplace, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(itemDecorator)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
        button_apply_promo.setOnClickListener {
            setButtonLoading(button_apply_promo, true)
            viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, R.raw.mutation_validate_use_promo_revamp))
        }
        button_apply_no_promo.setOnClickListener {
            setButtonLoading(button_apply_no_promo, true)
            viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo))
        }

        val lastHeaderUiModel: PromoListHeaderUiModel? = null
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                handleStickyPromoHeader(recyclerView, lastHeaderUiModel)
            }
        })

        observeFragmentUiModel()
        observePromoRecommendationUiModel()
        observePromoInputUiModel()
        observePromoListUiModel()
        observeVisitableChangeUiModel()
        observeVisitableListChangeUiModel()
        observeEmptyStateUiModel()
        observeApplyPromoResult()
        observeClearPromoResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
            reloadData()
        }
    }

    private fun setButtonLoading(button: UnifyButton, isLoading: Boolean) {
        if (isLoading) {
            button.isLoading = true
            button.isClickable = false
        } else {
            button.isLoading = false
            button.isClickable = true
        }
    }

    private fun handleStickyPromoHeader(recyclerView: RecyclerView, lastHeaderUiModel: PromoListHeaderUiModel?) {
        if (adapter.data.isNotEmpty()) {
            var tmpLastHeaderUiModel = lastHeaderUiModel
            val topItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val lastData = adapter.data[topItemPosition]

            val isShow: Boolean
            if (lastData is PromoListHeaderUiModel && lastData.uiState.isEnabled && !lastData.uiState.isExpanded) {
                tmpLastHeaderUiModel = lastData
                isShow = true
            } else if (tmpLastHeaderUiModel != null && lastData is PromoListItemUiModel && lastData.uiData.parentIdentifierId == tmpLastHeaderUiModel.uiData.identifierId && lastData.uiState.isParentEnabled) {
                isShow = true
            } else if (lastData is PromoListItemUiModel && lastData.uiState.isParentEnabled) {
                if (tmpLastHeaderUiModel != null && lastData.uiData.parentIdentifierId == tmpLastHeaderUiModel.uiData.identifierId) {
                    isShow = true
                } else {
                    var foundHeader = false
                    adapter.data.forEach {
                        if (it is PromoListHeaderUiModel && it.uiData.identifierId == lastData.uiData.parentIdentifierId) {
                            tmpLastHeaderUiModel = it
                            foundHeader = true
                            return@forEach
                        }
                    }
                    isShow = foundHeader
                }
            } else {
                isShow = false
            }

            // View logic here should be same as view logic on #PromoListHeaderEnabledViewHolder
            if (tmpLastHeaderUiModel != null) {
                if (tmpLastHeaderUiModel?.uiData?.iconUrl?.isNotBlank() == true) {
                    ImageHandler.loadImageRounded2(context, section_image_promo_list_header, tmpLastHeaderUiModel?.uiData?.iconUrl)
                    section_image_promo_list_header.show()
                } else {
                    section_image_promo_list_header.gone()
                }

                section_label_promo_list_header_title.text = tmpLastHeaderUiModel?.uiData?.title
                section_label_promo_list_header_sub_title.text = tmpLastHeaderUiModel?.uiData?.subTitle

                if (tmpLastHeaderUiModel?.uiState?.isExpanded == false) {
                    section_image_chevron.rotation = 180f
                } else {
                    section_image_chevron.rotation = 0f
                }

                setImageFilterNormal(section_image_promo_list_header)
                section_label_promo_list_header_sub_title.show()
                section_image_chevron.show()
                header_promo_section.setOnClickListener {
                    if (tmpLastHeaderUiModel != null) {
                        onClickPromoListHeader(tmpLastHeaderUiModel!!)
                    }
                }
            }

            if (isShow) {
                header_promo_section.show()
                setToolbarShadowVisibility(false)
            } else {
                header_promo_section.gone()
                setToolbarShadowVisibility(true)
            }
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (show) {
                app_bar_layout.elevation = HAS_ELEVATION.toFloat()
            } else {
                app_bar_layout.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(this, Observer {
            hideLoading()
            renderFragmentState(it)
        })
    }

    private fun addOrModify(it: Visitable<*>) {
        if (adapter.data.contains(it)) {
            adapter.modifyData(adapter.data.indexOf(it))
        } else {
            adapter.addVisitable(it)
        }
    }

    private fun observeEmptyStateUiModel() {
        viewModel.promoEmptyStateUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoRecommendationUiModel() {
        viewModel.promoRecommendationUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoInputUiModel() {
        viewModel.promoInputUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoListUiModel() {
        viewModel.promoListUiModel.observe(this, Observer {
            adapter.addVisitableList(it)
        })
    }

    private fun observeVisitableChangeUiModel() {
        viewModel.tmpUiModel.observe(this, Observer {
            when (it) {
                is Update -> {
                    adapter.modifyData(adapter.data.indexOf(it.data))
                }
                is Delete -> {
                    adapter.removeData(it.data)
                }
            }
        })
    }

    private fun observeVisitableListChangeUiModel() {
        viewModel.tmpListUiModel.observe(this, Observer {
            when (it) {
                is Insert -> {
                    it.data.forEach {
                        adapter.addVisitableList((adapter.data.indexOf(it.key) + 1), it.value)
                    }
                }
            }
        })
    }

    private fun observeApplyPromoResult() {
        viewModel.applyPromoResponse.observe(this, Observer {
            setButtonLoading(button_apply_promo, false)
            when {
                it.state == ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CART -> {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
                it.state == ApplyPromoResponseAction.ACTION_RELOAD_PROMO -> {
                    reloadData()
                }
                it.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    it.exception?.let {
                        showToastMessage(it)
                    }
                }
            }
        })
    }

    private fun observeClearPromoResult() {
        viewModel.clearPromoResponse.observe(this, Observer {
            setButtonLoading(button_apply_no_promo, false)
            when {
                it.state == ClearPromoResponseAction.ACTION_STATE_SUCCESS -> activity?.finish()
                it.state == ClearPromoResponseAction.ACTION_STATE_ERROR -> it.exception?.let {
                    showToastMessage(it)
                }
            }
        })
    }

    private fun renderFragmentState(fragmentUiModel: FragmentUiModel) {
        if (!fragmentUiModel.uiState.hasFailedToLoad) {
            if (fragmentUiModel.uiState.hasAnyPromoSelected) {
                toolbar?.enableResetButton()
                activity?.let {
                    label_total_promo_info.show()
                    label_total_promo_amount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.uiData.totalBenefit, false)
                    label_total_promo_amount.show()
                    if (fragmentUiModel.uiData.tokopointsTncLabel.isNotBlank()) {
                        label_tokopoints.text = fragmentUiModel.uiData.tokopointsTncLabel
                        label_tokopoints.setOnTouchListener { v, event ->
                            if (event.action == MotionEvent.ACTION_UP) {
                                val textLocation = IntArray(2)
                                label_tokopoints.getLocationOnScreen(textLocation)
                                if (event.rawX >= textLocation[0] + label_tokopoints.width - label_tokopoints.totalPaddingRight) {
                                    activity?.let {
                                        TokoPointsTncBottomsheet().apply {
                                            setBottomsheetData(fragmentUiModel.uiData.tokopointsTncTitle, fragmentUiModel.uiData.tokopointsTncDetails)
                                            show(it.supportFragmentManager, hashCode().toString())
                                        }
                                    }
                                }
                            }
                            true
                        }
                        label_tokopoints.show()
                    }
                    button_apply_promo.text = String.format(it.resources.getString(R.string.promo_checkout_label_button_apply_promo), fragmentUiModel.uiData.usedPromoCount)
                    button_apply_promo.show()
                    button_apply_no_promo.gone()
                    container_action_bottom.show()
                }
            } else {
                toolbar?.disableResetButton()
                if (fragmentUiModel.uiState.hasPreAppliedPromo) {
                    label_total_promo_info.gone()
                    label_total_promo_amount.gone()
                    button_apply_promo.gone()
                    button_apply_no_promo.show()
                    label_tokopoints.gone()
                    container_action_bottom.show()
                } else {
                    container_action_bottom.gone()
                }
            }
            layout_global_error.gone()
            layout_main_container.show()
        } else {
            toolbar?.disableResetButton()
            fragmentUiModel.uiData.exception?.let {
                layout_global_error.setType(getGlobalErrorType(it))
            }
            layout_global_error.setActionClickListener { view ->
                layout_global_error.gone()
                reloadData()
            }
            layout_global_error.show()
            layout_main_container.gone()
            container_action_bottom.gone()
        }
    }

    private fun reloadData() {
        adapter.clearAllElements()
        layout_main_container.show()
        loadData(0)
    }

    private fun getGlobalErrorType(e: Throwable): Int {
        return if (e is UnknownHostException) {
            NO_CONNECTION
        } else {
            SERVER_ERROR
        }
    }

    private fun setupToolbar(view: View) {
        activity?.let {
            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            appbar.removeAllViews()
            toolbar = toolbarPromoCheckout()
            toolbar?.let {
                appbar.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
            }
        }
    }

    private fun toolbarPromoCheckout(): ToolbarPromoCheckout? {
        activity?.let {
            return ToolbarPromoCheckout(it).apply {
                listener = this@PromoCheckoutFragment
            }
        }

        return null
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PromoCheckoutAdapterTypeFactory> {
        adapter = PromoCheckoutAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): PromoCheckoutAdapterTypeFactory {
        return PromoCheckoutAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun loadData(page: Int) {
        activity?.let {
            showLoading()
            val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) as PromoRequest
            val mutation = GraphqlHelper.loadRawString(it.resources, R.raw.get_coupon_list_recommendation)
            viewModel.loadData(mutation, promoRequest)
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    fun showToastMessage(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    fun showToastMessage(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage.isNotBlank()) {
            showToastMessage(errorMessage)
        } else {
            showToastMessage("Terjadi kesalahan. Ulangi beberapa saat lagi")
        }
    }

    private fun showSavePromoDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Simpan promo sebelum keluar?")
                setDescription("Kamu baru saja mengubah pilihan promo. Mau disimpan?")
                setPrimaryCTAText("Simpan Promo Baru")
                setSecondaryCTAText("Keluar Halaman")
                setPrimaryCTAClickListener {
                    if (viewModel.isHasAnySelectedPromoItem()) {
                        viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, R.raw.mutation_validate_use_promo_revamp))
                    } else {
                        viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo))
                    }
                }
                setSecondaryCTAClickListener {
                    dismiss()
                    it.finish()
                }
            }.show()
        }
    }

    override fun onBackPressed() {
        if (viewModel.fragmentUiModel.value != null) {
            if (viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false && viewModel.hasDifferentPreAppliedState()) {
                showSavePromoDialog()
            } else {
                activity?.finish()
            }
        } else {
            activity?.finish()
        }
    }

    override fun onClickResetPromo() {
        viewModel.resetPromo()
    }

    override fun onClickApplyRecommendedPromo() {
        viewModel.applyPromoSuggestion()
    }

    override fun onClickApplyManualInputPromo(promoCode: String) {
        activity?.let {
            viewModel.updatePromoInputState(promoCode)
            viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, R.raw.mutation_validate_use_promo_revamp), promoCode)
        }
    }

    override fun onClickPromoListHeader(element: PromoListHeaderUiModel) {
        viewModel.updatePromoListAfterClickPromoHeader(element)
    }

    override fun onClickPromoListItem(element: PromoListItemUiModel) {
        viewModel.updatePromoListAfterClickPromoItem(element)
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            putExtra(EXTRA_KUPON_CODE, element.uiData.promoCode)
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
        }
        startActivity(intent)
    }

    override fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel) {
        viewModel.updateIneligiblePromoList(element)
    }

    override fun onClickEmptyStateButton(element: PromoEmptyStateUiModel) {
        if (element.uiData.emptyStateStatus == STATUS_PHONE_NOT_VERIFIED) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
        }
    }

}