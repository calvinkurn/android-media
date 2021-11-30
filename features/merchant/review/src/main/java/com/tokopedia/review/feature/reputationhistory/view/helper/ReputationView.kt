package com.tokopedia.review.feature.reputationhistory.view.helper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.annotation.LayoutRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author normansyahputa on 3/20/17.
 */
class ReputationView(context: Context, attrs: AttributeSet) : FrameLayout(
    context, attrs
) {
    private var reputationPoints: Typography? = null
    private var reputationBadge: AppCompatImageView? = null

    @LayoutRes
    private val defaultLayoutId = R.layout.reputation_item_view_reputation

    fun init(data: ReputationShopUiModel) {
        reputationPoints?.text = String.format(
            "%s %s",
            data.reputationScore,
            context.getString(R.string.point)
        )
        reputationBadge?.loadImage(data.badgeReputationUrl)
    }

    init {
        LayoutInflater.from(context).inflate(defaultLayoutId, this)
        reputationPoints = findViewById(R.id.reputation_points)
        reputationBadge = findViewById(R.id.reputationBadge)
    }
}