package com.tokopedia.minicart.common.general

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheetListener
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheet
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheet
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartGeneralWidgetComponent
import com.tokopedia.minicart.databinding.WidgetMiniCartGeneralBinding
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

class MiniCartGeneralWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), MiniCartListBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var miniCartListBottomSheet: MiniCartListBottomSheet

    @Inject
    lateinit var miniCartChatListBottomSheet: MiniCartChatListBottomSheet

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    private var viewModel: MiniCartGeneralViewModel? = null
    private val binding: WidgetMiniCartGeneralBinding

    private var miniCartWidgetListener: MiniCartWidgetListener? = null

    init {
        binding = WidgetMiniCartGeneralBinding.inflate(LayoutInflater.from(context))
        val application = (context as? Activity)?.application
        initializeInjector(application)
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartGeneralWidgetComponent.builder()
                .baseAppComponent(baseAppComponent.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun initializeListener(listener: MiniCartWidgetListener) {
        this.miniCartWidgetListener = listener
    }

    private fun initializeViewModel(fragment: Fragment) {
        this.viewModel =
            ViewModelProvider(fragment, viewModelFactory).get(MiniCartGeneralViewModel::class.java)
        TODO("Not yet implemented")
    }

    /**
     * Function to initialize the widget
     */
    fun initialize(
        shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener,
        autoInitializeData: Boolean = true, isShopDirectPurchase: Boolean = true
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Function to trigger update mini cart data
     * This will trigger view model to fetch latest data from backend and update the UI
     */
    fun updateData(shopIds: List<String>) {
        TODO("Not yet implemented")
    }

    /**
     * Function to trigger update mini cart data
     * This will trigger widget to update the UI with provided data
     */
    fun updateData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        TODO("Not yet implemented")
    }

    /**
     * Function to trigger update simplified summary data
     * This will trigger widget to update the UI with provided data
     */
    fun updateData(/* TBD: miniCartSimplifiedSummaryData: MiniCartSimplifiedSummaryData */) {
        TODO("Not yet implemented")
    }

    /**
     * Function to show mini cart chat bottom sheet
     */
    private fun showMiniCartChatListBottomSheet(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    /**
     * Function to show mini cart bottom sheet
     */
    fun showMiniCartListBottomSheet(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    /**
     * Function to show simplified summary bottom sheet
     */
    fun showSimplifiedSummaryBottomSheet(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    override fun onMiniCartListBottomSheetDismissed() {
        TODO("Not yet implemented")
    }

    override fun onBottomSheetSuccessGoToCheckout() {
        TODO("Not yet implemented")
    }

    override fun onBottomSheetFailedGoToCheckout(
        toasterAnchorView: View,
        fragmentManager: FragmentManager,
        globalEvent: GlobalEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun showToaster(
        view: View?,
        message: String,
        type: Int,
        ctaText: String,
        isShowCta: Boolean,
        onClickListener: OnClickListener?
    ) {
        TODO("Not yet implemented")
    }

    override fun showProgressLoading() {
        TODO("Not yet implemented")
    }

    override fun hideProgressLoading() {
        TODO("Not yet implemented")
    }
}