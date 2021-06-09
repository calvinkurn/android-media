package com.tokopedia.mvcwidget.views

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.mvc_layout_bottomsheet_button.view.*
import javax.inject.Inject

class MvcDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MvcDetailViewContract {

    var rv: RecyclerView
    var viewFlipper: ViewFlipper
    var globalError: GlobalError
    var addBottomMarginOnToast = false

    private val widgetImpression = WidgetImpression()

    override fun getWidgetImpression(): WidgetImpression {
        return widgetImpression
    }

    private val adapter = MvcDetailAdapter(arrayListOf(), this)

    private val CONTAINER_CONTENT = 0
    private val CONTAINER_SHIMMER = 1
    private val CONTAINER_ERROR = 2
    private var shopId = ""
    override fun getShopId(): String {
        return this.shopId
    }

    @MvcSource
    private var mvcSource: Int = MvcSource.DEFAULT

    override fun getMvcSource(): Int {
        return mvcSource
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MvcDetailViewModel


    init {
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
            viewModel.getListData(shopId)
        }
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
            Tracker.viewJadiMemberToast(shopId, UserSession(context).userId, mvcSource, true)
        }
    }

    private fun handleMembershipRegistrationError(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleJadiMemberButtonClick()
            }).show()
            Tracker.viewJadiMemberToast(shopId, UserSession(context).userId, mvcSource, false)
        }
    }

    private fun handleFollowSuccess(message: String?) {
        if (!message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, message, Toast.LENGTH_SHORT).show()
            Tracker.viewFollowButtonToast(shopId, UserSession(context).userId, mvcSource, true)
        }
    }

    private fun handleFollowFail(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            setToastBottomMargin()
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleFollowButtonClick()
            }).show()
            Tracker.viewFollowButtonToast(shopId, UserSession(context).userId, mvcSource, false)
        }
    }

    fun show(shopId: String, addBottomMarginOnToast: Boolean, @MvcSource mvcSource: Int) {
        this.addBottomMarginOnToast = addBottomMarginOnToast
        this.shopId = shopId
        this.mvcSource = mvcSource
        viewModel.getListData(shopId)
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
                        ?: "", spannableString2)
                tempCouponList.add(mvcListItem)
            }
        }

        val shopName = response.data?.shopName
        if (!tempCouponList.isNullOrEmpty() && !shopName.isNullOrEmpty()) {
            tempList.add(TickerText(shopName, removeTickerTopMargin))
        }

        if(!tempCouponList.isNullOrEmpty()){
            tempList.addAll(tempCouponList)
        }

        adapter.updateList(tempList)
        if(!tempList.isNullOrEmpty()){
            if (response.data?.followWidget?.isShown == true && !response.data?.followWidget.type.isNullOrEmpty()) {
                Tracker.viewCoupons(response.data.followWidget.type,this.shopId, UserSession(context).userId, this.mvcSource)
            }else{
                Tracker.viewCoupons(FollowWidgetType.DEFAULT,this.shopId, UserSession(context).userId, this.mvcSource)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.listLiveData.removeObservers(context as AppCompatActivity)
        viewModel.membershipLiveData.removeObservers(context as AppCompatActivity)
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