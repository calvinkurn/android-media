package com.tokopedia.promotionstarget.presentation.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.CouponStatusType
import com.tokopedia.promotionstarget.data.coupon.CouponUiData
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationComponent
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.CmGratificationViewModel
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber
import javax.inject.Inject

class CmGratificationDialog {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CmGratificationViewModel

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var btnAction: Typography
    private lateinit var btnAction2: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var space: Space

    val couponUiDataList = arrayListOf<CouponUiData>()

    fun getLayout(): Int {
        return R.layout.dialog_gratification
    }

    fun show(activityContext: Context, gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse) {
        val pair = prepareBottomSheet(activityContext)
        initViews(pair.first, activityContext, gratifNotification, couponDetailResponse)
        setUiData(gratifNotification, couponDetailResponse)
    }

    private fun prepareBottomSheet(activityContext: Context): Pair<View, BottomSheetDialog> {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(activityContext, {})
        val view = LayoutInflater.from(activityContext).inflate(getLayout(), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()
        return Pair(view, bottomSheet)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context,
                          gratifNotification: GratifNotification,
                          couponDetailResponse: TokopointsCouponDetailResponse) {
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)
        btnAction2 = root.findViewById(R.id.btnAction2)
        space = root.findViewById(R.id.space)
        recyclerView = root.findViewById(R.id.recyclerView)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        progressBar = root.findViewById(R.id.targetProgressBar)

        try {
            val imageClose = (root.parent.parent as ConstraintLayout).findViewById<ImageView>(R.id.close_button_rounded)
            imageClose.setImageResource(R.drawable.t_promo_close)

            val dialogTitleText = couponDetailResponse.coupon?.couponStatus?.let {
                when (it) {
                    CouponStatusType.USED -> gratifNotification.wordingUsed?.title
                    CouponStatusType.EXPIRED -> gratifNotification.wordingExpired?.title
                    CouponStatusType.ACTIVE -> gratifNotification.wordingActive?.title
                    else -> ""
                }
            }
            val tvDialogTitle = (root.parent.parent as ConstraintLayout).findViewById<TextView>(R.id.title_closeable_rounded)
            tvDialogTitle.text = dialogTitleText
        } catch (th: Throwable) {
            Timber.d(th)
        }

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(CouponItemDecoration())
        couponListAdapter = CouponListAdapter(couponUiDataList)

        initInjections(activityContext)
    }

    private fun initInjections(activityContext: Context) {
        val activity = activityContext as AppCompatActivity

        val component = DaggerCmGratificationComponent.builder()
                .appModule(AppModule(activity.application))
                .build()
        component.inject(this)
        activity.run {
            val viewModelProvider = ViewModelProviders.of(activityContext, viewModelFactory)
            viewModel = viewModelProvider[CmGratificationViewModel::class.java]
        }
    }


    private fun setUiData(gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse) {
        val pair = couponDetailResponse.coupon?.couponStatus?.let {
            when (it) {
                CouponStatusType.USED -> gratifNotification.wordingUsed?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                CouponStatusType.EXPIRED -> gratifNotification.wordingExpired?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                CouponStatusType.ACTIVE -> gratifNotification.wordingActive?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                else -> Pair("", "")
            }
        }
        tvTitle.text = pair?.first
        tvSubTitle.text = pair?.first

        //set coupon data
//        if (couponDetailResponse.couponList != null && couponDetailResponse.couponList.isNotEmpty()) {
        if (couponDetailResponse.coupon != null) {
            couponUiDataList.add(couponDetailResponse.coupon)
            couponListAdapter.notifyDataSetChanged()
        }

        //second button
        val showSecondButton = gratifNotification.secondButton?.isShown != null && gratifNotification.secondButton.isShown
        toggleSecondButton(showSecondButton)
        if (showSecondButton) {
            btnAction2.text = gratifNotification.secondButton?.text
            btnAction2.setOnClickListener {
                if (!gratifNotification.secondButton?.applink.isNullOrEmpty()) {
                    RouteManager.route(it.context, gratifNotification.secondButton?.applink)
                }
            }
        }
    }

    fun toggleSecondButton(toggle: Boolean) {
        if (toggle) {
            btnAction2.visibility = View.VISIBLE
            space.visibility = View.VISIBLE
        } else {
            btnAction2.visibility = View.GONE
            space.visibility = View.GONE
        }
    }
}