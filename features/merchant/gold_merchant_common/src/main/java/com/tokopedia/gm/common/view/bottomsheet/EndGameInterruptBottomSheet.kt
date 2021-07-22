package com.tokopedia.gm.common.view.bottomsheet

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class EndGameInterruptBottomSheet: BaseBottomSheet() {

    private var tvEndGameInterrupt: Typography? = null
    private var tvTitleCardEndGame: Typography? = null
    private var tvDescCardEndGame: Typography? = null
    private var tvStatusEndGameInterrupt: Typography? = null
    private var btnCheckShopPerformance: UnifyButton? = null

    override fun getResLayout(): Int = R.layout.bottom_sheet_end_game_interrupt_shop_score

    override fun setupView() = childView?.run {
        setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        tvEndGameInterrupt = findViewById(R.id.tvEndGameInterrupt)
        tvTitleCardEndGame = findViewById(R.id.tvTitleCardEndGame)
        tvDescCardEndGame = findViewById(R.id.tvDescCardEndGame)
        tvStatusEndGameInterrupt = findViewById(R.id.tvStatusEndGameInterrupt)
        btnCheckShopPerformance = findViewById(R.id.btnCheckShopPerformance)
        setTextView()
    }

    fun setTextView() {
        val titleEndGameInterrupt = arguments?.getString(TITLE_END_GAME_INTERRUPT_KEY).orEmpty()
        val titleCardEndGameInterrupt = arguments?.getString(TITLE_CARD_END_GAME_INTERRUPT_KEY).orEmpty()
        val descCardEndGameInterrupt = arguments?.getString(DESC_CARD_END_GAME_INTERRUPT_KEY).orEmpty()
        tvEndGameInterrupt?.text = titleEndGameInterrupt
        tvTitleCardEndGame?.text = titleCardEndGameInterrupt
        tvDescCardEndGame?.text = descCardEndGameInterrupt
        tvStatusEndGameInterrupt?.text = MethodChecker.fromHtml(getString(R.string.status_end_game_period_information_interrupt))
    }

    fun btnClickGotoShopScore(action:() -> Unit) {
        btnCheckShopPerformance?.setOnClickListener {
            action()
            dismiss()
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    companion object {
        private const val TAG = "EndGameInterruptBottomSheet"
        private const val TITLE_END_GAME_INTERRUPT_KEY = "title_end_game_interrupt_key"
        private const val TITLE_CARD_END_GAME_INTERRUPT_KEY = "title_card_end_game_interrupt_key"
        private const val DESC_CARD_END_GAME_INTERRUPT_KEY = "desc_card_end_game_interrupt_key"

        fun createInstance(titleEndGame: String, titleCardEndGame: String, descCardEndGame: String): EndGameInterruptBottomSheet {
            return EndGameInterruptBottomSheet().apply {
                val args = Bundle()
                args.putString(TITLE_END_GAME_INTERRUPT_KEY, titleEndGame)
                args.putString(TITLE_CARD_END_GAME_INTERRUPT_KEY, titleCardEndGame)
                args.putString(DESC_CARD_END_GAME_INTERRUPT_KEY, descCardEndGame)
                showKnob = true
                arguments = args
            }
        }
    }
}