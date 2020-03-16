package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.PostListUiModel
import com.tokopedia.centralizedpromo.view.model.PostUiModel
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.centralized_promo_partial_post.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_post_error.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_post_shimmering.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_post_success.view.*
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*

class PartialCentralizedPromoPostView(
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        showCoachMark: Boolean
) : BasePartialListView<PostListUiModel, CentralizedPromoAdapterTypeFactory, PostUiModel>(view, adapterTypeFactory, coachMarkListener, showCoachMark) {

    init {
        setupPostRecycler()
    }

    private fun setupPostRecycler() = with(view) {
        rvCentralizedPromoPostList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PartialCentralizedPromoPostView.adapter.apply { setHasStableIds(true) }
            isNestedScrollingEnabled = false
        }
    }

    override fun renderError(cause: Throwable) {
        with(view) {
            show()
            layoutCentralizedPromoPostListShimmering.hide()
            layoutCentralizedPromoPostListSuccess.hide()
            if (stubLayoutCentralizedPromoPostListError?.parent != null) {
                stubLayoutCentralizedPromoPostListError.inflate()
            }
            layoutCentralizedPromoPostListError?.show()
            ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
        }
        super.renderError(cause)
    }

    override fun renderLoading() = with(view) {
        show()
        layoutCentralizedPromoPostListSuccess.hide()
        layoutCentralizedPromoPostListError?.hide()
        layoutCentralizedPromoPostListShimmering.show()
    }

    override fun bindSuccessData(data: PostListUiModel) = with(view) {
        tvCentralizedPromoPostListTitle.text = context.getString(R.string.sah_label_tips_and_trick)
        show()
    }

    override fun onRecyclerViewResultDispatched() = with(view) {
        layoutCentralizedPromoPostListShimmering.hide()
        layoutCentralizedPromoPostListError?.hide()
        layoutCentralizedPromoPostListSuccess.show()

        addOnImpressionListener(impressHolder) {
            CentralizedPromoTracking.sendImpressionArticle()
        }
    }

    override fun shouldShowCoachMark(): Boolean = false

    override fun getCoachMarkItem(): CoachMarkItem? = null

    override fun onRecyclerViewItemEmpty() = view.gone()
}