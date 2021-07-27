package com.tokopedia.review.feature.reputationhistory.view.helper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationView.ReputationUiModel
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
    private var reputationPoints: Typography
    private var reputationBadge: AppCompatImageView

    @LayoutRes
    private val defaultLayoutId: Int = R.layout.reputation_item_view_reputation

    fun init(data: ReputationShopUiModel) {
        reputationPoints.text = String.format(
            "%s %s",
            data.reputationScore,
            context.getString(R.string.point)
        )
        reputationBadge.loadImage(data.badgeReputationUrl)
    }

    data class ReputationUiModel(
        @JvmField
        var reputationBadgeUrl: String? = "",
        @JvmField
        var reputationScore: String = ""
    )

    init {
        LayoutInflater.from(context).inflate(defaultLayoutId, this)
        reputationPoints = findViewById(R.id.reputation_points)
        reputationBadge = findViewById(R.id.reputationBadge)
    }
}