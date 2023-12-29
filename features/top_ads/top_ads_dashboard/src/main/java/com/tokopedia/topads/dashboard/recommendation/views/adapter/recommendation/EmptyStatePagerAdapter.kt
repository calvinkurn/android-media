package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_PRODUK_IKLAN
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStatesUiModel
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.util.Locale

class EmptyStatePagerAdapter : RecyclerView.Adapter<EmptyStatePagerAdapter.ViewHolder>() {

    var emptyStatePages = listOf<EmptyStatesUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.empty_state_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = emptyStatePages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = emptyStatePages.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val emptyStateTitle: Typography = view.findViewById(R.id.emptyStateTitle)
        private val emptyStateImage: ImageUnify =
            view.findViewById(R.id.emptyStateImage)
        private val stateType: Typography = view.findViewById(R.id.stateType)
        private val stateTypeDescription: Typography = view.findViewById(R.id.stateTypeDescription)
        private val btnEmptyState: UnifyButton = view.findViewById(R.id.btnEmptyState)
        fun bind(item: EmptyStatesUiModel) {
            emptyStateTitle.text = item.heading
            stateType.text = item.stateType
            stateTypeDescription.text = item.stateTypeDescription
            emptyStateImage.loadImage(item.imageUrl)
            if (item.buttonText.isEmpty()) {
                btnEmptyState.invisible()
            } else {
                btnEmptyState.text = item.buttonText
                btnEmptyState.show()
            }
            setClickAction(item.landingUrl)
        }

        private fun setClickAction(landingPage: String) {
            if (landingPage.isEmpty()) return
            btnEmptyState.setOnClickListener {
                when (landingPage) {
                    "$PARAM_PRODUK_IKLAN" -> {
                        if (view.context is TopAdsDashboardActivity) {
                            (view.context as TopAdsDashboardActivity).switchTab(landingPage.toIntOrZero())
                        } else {
                            val intent = RouteManager.getIntent(
                                view.context,
                                ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
                            )
                            intent.putExtra(
                                TopAdsCommonConstant.TOPADS_MOVE_TO_DASHBOARD,
                                landingPage.toIntOrZero()
                            )
                            view.context.startActivity(intent)
                        }
                    }
                    ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP -> RouteManager.route(
                        view.context,
                        landingPage
                    )
                    RecommendationConstants.SEARCH_REPORT_EDU_URL -> RouteManager.route(
                        view.context,
                        String.format(
                            Locale.getDefault(),
                            view.context.getString(R.string.topads_url_format_template),
                            ApplinkConst.WEBVIEW,
                            landingPage
                        )
                    )
                }
            }
        }
    }
}
