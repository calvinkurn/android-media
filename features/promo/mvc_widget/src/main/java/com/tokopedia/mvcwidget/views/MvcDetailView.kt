package com.tokopedia.mvcwidget.views

import android.content.Context
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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.di.components.DaggerMvcComponent
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class MvcDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MvcDetailViewContract {

    var rv: RecyclerView
    var viewFlipper: ViewFlipper
    var globalError: GlobalError

    private val adapter = MvcDetailAdapter(arrayListOf(), this)

    private val CONTAINER_CONTENT = 0
    private val CONTAINER_SHIMMER = 1
    private val CONTAINER_ERROR = 2
    var shopId = ""

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
        if (!message.isNullOrEmpty())
            Toaster.build(rootView, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleMembershipRegistrationError(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleJadiMemberButtonClick()
            }).show()
        }
    }

    private fun handleFollowSuccess(message: String?) {
//        Toaster.toasterCustomBottomHeight = dpToPx(48).toInt()
        if (!message.isNullOrEmpty()) {
            Toaster.build(rootView, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFollowFail(th: Throwable?) {
        toggleLoading(false)
        if (!th?.message.isNullOrEmpty()) {
            Toaster.build(rootView, th!!.message!!, Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, context.getString(R.string.mvc_coba_lagi), OnClickListener {
                handleFollowButtonClick()
            }).show()
        }
    }

    fun show(shopId: String) {
        this.shopId = shopId
        viewModel.getListData(shopId)
    }

    fun setupData(response: TokopointsCatalogMVCListResponse) {
        val tempList = arrayListOf<MvcListItem>()
        response.data?.followWidget?.let {
            tempList.add(it)
        }

        val shopName = response.data?.shopName
        if (!shopName.isNullOrEmpty()) {
            tempList.add(TickerText(shopName))
        }

        if(response.data?.followWidget?.isShown == true){
            response.data.catalogList?.forEach {
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

                    spannableString2.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.mvcw_red)), sb.toString().length - quotaTextLength, sb.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val mvcListItem = MvcCouponListItem(it.tagImageURLs, it.title ?: "", it.minimumUsageLabel
                            ?: "", spannableString2)
                    tempList.add(mvcListItem)
                }
            }
        }

        adapter.updateList(tempList)
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
}