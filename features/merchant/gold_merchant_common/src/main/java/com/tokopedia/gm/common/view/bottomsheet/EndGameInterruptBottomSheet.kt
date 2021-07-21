package com.tokopedia.gm.common.view.bottomsheet

import androidx.core.content.ContextCompat
import com.tokopedia.gm.common.R

class EndGameInterruptBottomSheet: BaseBottomSheet() {

    override fun getResLayout(): Int = R.layout.bottom_sheet_end_game_interrupt_shop_score

    override fun setupView() = childView?.run {
        setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}