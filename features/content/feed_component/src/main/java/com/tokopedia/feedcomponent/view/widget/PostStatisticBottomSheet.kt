package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.RouteManagerKt
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.statistic.PostStatisticAdapter
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticPlaceholderUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify

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

    override fun onSeeMoreDetailClicked(type: PostStatisticDetailType) { listener?.onSeeMoreDetailClicked(this, type) }

    fun setPostStatisticCommissionModel(model: PostStatisticCommissionUiModel) {
        tvProductsCommission.text = model.totalCommission
        statisticAdapter.setItemsAndAnimateChanges(model.postStatisticList)

        ivLoading.hide()
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
        }
    }

    private fun initLoading(view: View) {
        Glide.with(view)
                .asGif()
                .load(R.drawable.ic_feed_loading_indeterminate)
                .into(ivLoading)
    }

    private fun resetData() {
        ivLoading.show()
        groupDetail.invisible()
    }

    interface Listener {
        fun onGetPostStatisticModelList(bottomSheet: PostStatisticBottomSheet, activityId: String, productIds: List<String>)

        fun onSeeMoreDetailClicked(bottomSheet: PostStatisticBottomSheet, type: PostStatisticDetailType)
    }
}