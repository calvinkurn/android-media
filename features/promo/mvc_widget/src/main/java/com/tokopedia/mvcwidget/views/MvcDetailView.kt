package com.tokopedia.mvcwidget.views

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.mvcwidget.CtaCatalog
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.IntentManger
import com.tokopedia.mvcwidget.LiveDataResult
import com.tokopedia.mvcwidget.MvcCouponListItem
import com.tokopedia.mvcwidget.MvcDetailViewModel
import com.tokopedia.mvcwidget.MvcListItem
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.TickerText
import com.tokopedia.mvcwidget.TokopointsCatalogMVCListResponse
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class MvcDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MvcDetailViewContract {

    var rv: RecyclerView
    var viewFlipper: ViewFlipper
    var globalError: GlobalError
    var addBottomMarginOnToast = false
    var userSession: UserSession? = null

    private val widgetImpression = WidgetImpression()
    @FollowWidgetType var widgetType : String = FollowWidgetType.DEFAULT

    override fun getWidgetImpression(): WidgetImpression {
        return widgetImpression
    }

    val adapter by lazy { MvcDetailAdapter(arrayListOf(), this) }

    private val CONTAINER_CONTENT = 0
    private val CONTAINER_SHIMMER = 1
    private val CONTAINER_ERROR = 2
    private var shopId = ""
    private var productId = ""

    override fun getProductId(): String {
        return this.productId
    }

    var bundleForDataUpdate:Bundle? = null

    override fun getShopId(): String {
        return this.shopId
    }

    @MvcSource
    private var mvcSource: Int = MvcSource.DEFAULT

    private var mvcTracker : MvcTracker? = null

    override fun getMvcTracker(): MvcTracker? {
        return mvcTracker
    }

    override fun getMvcSource(): Int {
        return mvcSource
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MvcDetailViewModel


    init {
        userSession = UserSession(context)
        View.inflate(context, R.layout.mvc_detail_view, this)
        rv = findViewById(R.id.rv)
        viewFlipper = findViewById(R.id.viewFlipper)
        globalError = findViewById(R.id.mvcDetailGlobalError)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

         DaggerMvcComponent.builder()
            .build().inject(this)

        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context, viewModelFactory)
            viewModel = viewModelProvider[MvcDetailViewModel::class.java]
        }

        setupListeners()
    }

    fun setupListeners() {
        viewModel.listLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleError(it.error)
                }
            }
        })
        viewModel.membershipLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleMembershipRegistrationSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleMembershipRegistrationError(it.error)
                }
            }
        })
        viewModel.mvcSummatLiveData.observe(context as AppCompatActivity, Observer {
            if (it.status == LiveDataResult.STATUS.SUCCESS && it.data!=null) {
                handleMvcDataChanged(it.data)
            }
        })

        viewModel.followLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    toggleLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    handleFollowSuccess(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleFollowFail(it.error)
                }
            }
        })


        globalError.setActionClickListener {
            viewModel.getListData(shopId, productId = productId, source = mvcSource)
        }
    }


    private fun handleMvcDataChanged(data:TokopointsCatalogMVCSummaryResponse){
        bundleForDataUpdate = IntentManger.prepareBundleForJadiMember(data, shopId)
    }

    private fun toggleLoading(showLoading: Boolean) {
        if (showLoading) {
            viewFlipper.displayedChild = CONTAINER_SHIMMER
        } else {
            viewFlipper.displayedChild = CONTAINER_CONTENT
        }
    }

    private fun handleError(th: Throwable?) {
        viewFlipper.displayedChild = CONTAINER_ERROR
    }

    private fun handleSuccess(tokopointsCatalogMVCListResponse: TokopointsCatalogMVCListResponse?) {
        if (tokopointsCatalogMVCListResponse == null) handleError(null)
        tokopointsCatalogMVCListResponse?.let { response ->
            toggleLoading(false)
            setupData(response)
        }
    }

    private fun handleMembershipRegistrationSuccess(message: String?) {
        if (!message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, message, Toast.LENGTH_SHORT).show()
            mvcTracker?.viewJadiMemberToast(shopId, userSession?.userId, mvcSource, true)
        }
    }

    private fun handleMembershipRegistrationError(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleJadiMemberButtonClick()
            }).show()
            mvcTracker?.viewJadiMemberToast(shopId, userSession?.userId, mvcSource, false)
        }
    }

    private fun handleFollowSuccess(message: String?) {
        if (!message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, message, Toast.LENGTH_SHORT).show()
            mvcTracker?.viewFollowButtonToast(shopId, userSession?.userId, mvcSource, true)
        }
    }

    private fun handleFollowFail(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleFollowButtonClick()
            }).show()
            mvcTracker?.viewFollowButtonToast(shopId, userSession?.userId, mvcSource, false)
        }
    }

    fun show(shopId: String, addBottomMarginOnToast: Boolean, @MvcSource mvcSource: Int, mvcTracker: MvcTracker?, productId: String) {
        this.addBottomMarginOnToast = addBottomMarginOnToast
        this.mvcTracker = mvcTracker
        this.shopId = shopId
        this.productId = productId
        this.mvcSource = mvcSource
        viewModel.getListData(shopId, productId, source = mvcSource)
        this.adapter.setTracker(mvcTracker)
    }

    fun setupData(response: TokopointsCatalogMVCListResponse) {
        var removeTickerTopMargin = false
        val tempList = arrayListOf<MvcListItem>()

        response.data?.followWidget?.let {
            if (it.isShown == true) {
                tempList.add(0,it)
            } else {
                removeTickerTopMargin = true
            }
        }

        val tempCouponList = arrayListOf<MvcListItem>()
        response.data?.catalogList?.forEach {
            var quotaTextLength = 0
            if (it != null) {

                val sb = StringBuilder()
                if (!it.expiredLabel.isNullOrEmpty()) {
                    sb.append(it.expiredLabel)
                }
                if (!it.quotaLeftLabel.isNullOrEmpty()) {
                    if (sb.isNotEmpty()) {
                        sb.append(" • ")
                    }
                    quotaTextLength = it.quotaLeftLabel.length
                    sb.append(it.quotaLeftLabel)
                }
                val spannableString2 = SpannableString(sb.toString())

                spannableString2.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600)), sb.toString().length - quotaTextLength, sb.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val mvcListItem = MvcCouponListItem(it.tagImageURLs, it.title ?: "", it.minimumUsageLabel
                        ?: "", spannableString2, it.ctaCatalog?:CtaCatalog())
                tempCouponList.add(mvcListItem)
            }
        }

        val shopName = response.data?.shopName
        val formattedShopName = MethodChecker.fromHtml(shopName)
        if (!tempCouponList.isNullOrEmpty() && !formattedShopName.isNullOrEmpty()) {
            tempList.add(TickerText(formattedShopName, removeTickerTopMargin))
        }

        if(!tempCouponList.isNullOrEmpty()){
            tempList.addAll(tempCouponList)
        }

        adapter.updateList(tempList)
        if(!tempList.isNullOrEmpty()){
            if (response.data?.followWidget?.isShown == true && !response.data?.followWidget.type.isNullOrEmpty()) {
                widgetType = response.data.followWidget.type
                mvcTracker?.viewCoupons(widgetType,this.shopId, userSession?.userId, this.mvcSource)
            }else{
                mvcTracker?.viewCoupons(FollowWidgetType.DEFAULT,this.shopId, userSession?.userId, this.mvcSource)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.listLiveData.removeObservers(context as AppCompatActivity)
        viewModel.membershipLiveData.removeObservers(context as AppCompatActivity)
        viewModel.mvcSummatLiveData.removeObservers(context as AppCompatActivity)
    }

    override fun handleFollowButtonClick() {
        viewModel.followShop()
    }

    override fun handleJadiMemberButtonClick() {
        viewModel.registerMembership()
    }

    private fun setToastBottomMargin() {
        if (addBottomMarginOnToast) {
            Toaster.toasterCustomBottomHeight = dpToPx(48).toInt()
        }
    }
}

data class WidgetImpression(var sentFollowWidgetImpression: Boolean = false, var sentJadiMemberImpression: Boolean = false)
