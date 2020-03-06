package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.model.PostListUiModel
import com.tokopedia.centralized_promo.view.model.PostUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_post.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_post_error.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_post_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_post_success.view.*
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*

class PartialCentralizedPromoPostView(
        private val view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory
) : PartialView<PostListUiModel, CentralizedPromoAdapterTypeFactory, PostUiModel>(adapterTypeFactory) {

    init {
        setupPostRecycler()
    }

    private fun setupPostRecycler() {
        with(view) {
            rvCentralizedPromoPostList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@PartialCentralizedPromoPostView.adapter
            }
        }
    }

    override fun renderData(data: PostListUiModel) {
        with(view) {
            if (data.posts.isNotEmpty()) {
                tvCentralizedPromoPostListTitle.text = context.getString(R.string.sah_label_tips_and_trick)
                adapter.setElements(data.posts)
                show()
            } else {
                gone()
            }
            layoutCentralizedPromoPostListShimmering.hide()
            layoutCentralizedPromoPostListError?.hide()
            layoutCentralizedPromoPostListSuccess.show()
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
    }

    override fun onRefresh() {
        with(view) {
            show()
            layoutCentralizedPromoPostListSuccess.hide()
            layoutCentralizedPromoPostListError?.hide()
            layoutCentralizedPromoPostListShimmering.show()
        }
    }
}