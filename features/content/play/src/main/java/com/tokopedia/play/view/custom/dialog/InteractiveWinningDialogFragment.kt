package com.tokopedia.play.view.custom.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by jegul on 29/06/21
 */
class InteractiveWinningDialogFragment @Inject constructor(): DialogFragment() {

    private var mTitle = ""
    private var mSubtitle = ""
    private var mImageUrl = ""
    private var mGameUiModel: GameUiModel? = null

    private lateinit var tvTitle: Typography
    private lateinit var tvDetail: Typography
    private lateinit var imgUser: ImageUnify
    private lateinit var vRoot: RoundedConstraintLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_interactive_winning_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes ?: return
        params.width = context?.resources?.getDimensionPixelSize(R.dimen.play_interactive_winning_dialog_width) ?: 0
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params

        setupView()
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) {
            showNow(manager, TAG)
            view?.postDelayed({
                view?.let { dismiss() }
            }, DISMISS_DELAY)
        }
    }

    fun setData(imageUrl: String, title: String, subtitle: String, game: GameUiModel) {
        mImageUrl = imageUrl
        mTitle = title
        mSubtitle = subtitle
        mGameUiModel = game

        setupView()
    }

    private fun getInteractive(game: GameUiModel): Int {
        return when (game){
            is GameUiModel.Giveaway -> {
                commonR.drawable.bg_play_interactive
            }
            is GameUiModel.Quiz -> {
                commonR.drawable.bg_quiz_winner
            }
            else -> commonR.drawable.bg_play_interactive
        }
    }

    private fun initView(view: View) {
        with(view) {
            tvTitle = findViewById(R.id.tv_title)
            tvDetail = findViewById(R.id.tv_detail)
            imgUser = findViewById(R.id.img_user)
            vRoot = findViewById(R.id.rc_winning_dialog)
        }
    }

    private fun setupView() {
        if (::tvTitle.isInitialized) tvTitle.text = mTitle
        if (::tvDetail.isInitialized) tvDetail.text = mSubtitle
        if (::imgUser.isInitialized) imgUser.loadImage(mImageUrl)
        if(::vRoot.isInitialized) vRoot.background = MethodChecker.getDrawable(context, getInteractive(mGameUiModel ?: GameUiModel.Unknown))
    }

    companion object {

        private const val TAG = "Interactive Winning Dialog"
        private const val DISMISS_DELAY = 3000L

        fun get(manager: FragmentManager): InteractiveWinningDialogFragment? {
            return manager.findFragmentByTag(TAG) as? InteractiveWinningDialogFragment
        }
    }
}
