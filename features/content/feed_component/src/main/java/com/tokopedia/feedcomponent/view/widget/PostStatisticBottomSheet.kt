package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.statistic.PostStatisticAdapter
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticPlaceholderUiModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedcomponent.view.decor.PostStatisticItemDecoration


/**
 * Created by jegul on 2019-11-21
 */
class PostStatisticBottomSheet : BottomSheetUnify(), PostStatisticAdapter.Listener {

    companion object {

        private const val DETAIL_COUNT = 4

        @JvmStatic
        fun newInstance(context: Context): PostStatisticBottomSheet {
            return PostStatisticBottomSheet().apply {
                arguments = Bundle.EMPTY
                val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_post_statistic, null)
                setChild(view)
                initView(view)
                setCloseClickListener { this.dismiss() }
            }
        }
    }
    private val statisticAdapter = PostStatisticAdapter(this).apply {
        setItems(List(DETAIL_COUNT) { PostStatisticPlaceholderUiModel })
    }

    private var listener: Listener? = null
    private lateinit var tvProductsCommission: TextView
    private lateinit var rvStatistic: RecyclerView
    private lateinit var ivLoading: AppCompatImageView
    private lateinit var groupDetail: Group
    private lateinit var tvCheckDashboard: TextView

    private var globalError: View? = null

    override fun onSeeMoreDetailClicked(type: PostStatisticDetailType) { listener?.onSeeMoreDetailClicked(this, type) }

    fun setPostStatisticCommissionModel(model: PostStatisticCommissionUiModel) {
        tvProductsCommission.text = model.totalCommission
        statisticAdapter.setItemsAndAnimateChanges(model.postStatisticList)

        ivLoading.hide()
        globalError?.hide()
        groupDetail.show()
    }

    fun show(fragmentManager: FragmentManager,
             activityId: String,
             title: String,
             productIds: List<String>,
             listener: Listener) {
        resetData()
        this.listener = listener
        listener.onGetPostStatisticModelList(this, activityId, productIds)
        setTitle(title)
        show(fragmentManager, activityId)
    }

    fun setError(throwable: Throwable, activityId: String, productIds: List<String>) {
        if (view is ViewGroup) {
            val currentViewGroup = (view as ViewGroup)

            val clLayout = currentViewGroup.findViewById<ConstraintLayout>(R.id.cl_post_statistic)

            ivLoading.hide()
            groupDetail.invisible()

            if (clLayout != null) {

                clLayout.showUnifyError(
                        throwable,
                        { listener?.onGetPostStatisticModelList(this, activityId, productIds) }
                )

                if (globalError == null) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(clLayout)
                    constraintSet.connect(com.tokopedia.globalerror.R.id.globalerror_parent, ConstraintSet.TOP, clLayout.id, ConstraintSet.TOP)
                    constraintSet.connect(com.tokopedia.globalerror.R.id.globalerror_parent, ConstraintSet.BOTTOM, clLayout.id, ConstraintSet.BOTTOM, 24)
                    constraintSet.centerHorizontally(com.tokopedia.globalerror.R.id.globalerror_parent, clLayout.id)
                    constraintSet.applyTo(clLayout)

                    clLayout.findViewById<GlobalError>(com.tokopedia.globalerror.R.id.globalerror_view)?.apply {
                        gravity = Gravity.CENTER
                    }

                    globalError = clLayout.findViewById(com.tokopedia.globalerror.R.id.globalerror_parent)
                }
            }
        }
    }

    private fun initView(view: View) {
        tvProductsCommission = view.findViewById(R.id.tv_products_commission)
        rvStatistic = view.findViewById(R.id.rv_statistic)
        ivLoading = view.findViewById(R.id.iv_loading)
        groupDetail = view.findViewById(R.id.group_detail)
        tvCheckDashboard = view.findViewById(R.id.tv_check_dashboard)

        setupListener()
        setupList(view)
        initLoading(view)
    }

    private fun setupListener() {

        tvCheckDashboard.setOnClickListener {
            RouteManager.route(
                    it.context,
                    ApplinkConst.AFFILIATE_DASHBOARD
            )
            dismiss()
        }
    }

    private fun setupList(view: View) {
        rvStatistic.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = statisticAdapter
            addItemDecoration(PostStatisticItemDecoration(view.context))
        }
    }

    private fun initLoading(view: View) {
        Glide.with(view)
                .asGif()
                .load(R.drawable.ic_loading_indeterminate)
                .into(ivLoading)
    }

    private fun resetData() {
        ivLoading.show()
        globalError?.hide()
        groupDetail.invisible()
    }

    interface Listener {
        fun onGetPostStatisticModelList(bottomSheet: PostStatisticBottomSheet, activityId: String, productIds: List<String>)

        fun onSeeMoreDetailClicked(bottomSheet: PostStatisticBottomSheet, type: PostStatisticDetailType)
    }
}