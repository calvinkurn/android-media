package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.databinding.ItemBpcSpecBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.bullets.SpecBulletAdapter

/**
 * Created by Frenzel
 */
class BpcSpecsItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private var binding: ItemBpcSpecBinding? by viewBinding()
    private val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context) }

    fun bind(specsModel: BpcSpecsModel) {
        binding?.run {
            tvSpecTitle.text = specsModel.specsTitle
            if(specsModel.specsSummary.isNotEmpty()) {
                tvSpecSummary.text = MethodChecker.fromHtml(specsModel.specsSummary)
                rvSpecsBullet.gone()
                tvSpecSummary.visible()
            } else if(specsModel.specsSummaryBullet.isNotEmpty()) {
                rvSpecsBullet.layoutManager = layoutManager
                rvSpecsBullet.adapter = SpecBulletAdapter(specsModel.specsSummaryBullet)
                tvSpecSummary.gone()
                rvSpecsBullet.visible()
            }

            val drawable = ContextCompat.getDrawable(view.context, specsModel.bgDrawableRef)
            drawable?.let {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(drawable),
                    ContextCompat.getColor(view.context, specsModel.bgDrawableColorRef)
                )
            }
            holderSpecs.background = drawable
        }
    }
}
