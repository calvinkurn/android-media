package com.tokopedia.content.common.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.unifyprinciples.R.color.Unify_NN1000
import com.tokopedia.unifyprinciples.R.color.Unify_NN950
import com.tokopedia.content.common.R.color.content_dms_soft_dark
import com.tokopedia.content.common.R.color.content_dms_soft_gray
import com.tokopedia.content.common.databinding.ContentAccountToolbarBinding
import com.tokopedia.content.common.ui.toolbar.ContentColor.*
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.R.color.Unify_NN0
import com.tokopedia.unifyprinciples.R.color.Unify_NN400
import com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
import com.tokopedia.unifyprinciples.R.color.Unify_Static_White
import com.tokopedia.content.common.R as contentCommonR

class ContentAccountToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : Toolbar(context, attrs) {

    private var mOnClickListener: AccountClickListener? = null
    private var mOnBackListener: BackClickListener? = null
    private var mBinding = ContentAccountToolbarBinding.inflate(LayoutInflater.from(context), this)
    private var coachMark: CoachMark2? = null

    init {
        initViews()

        showHideExpandIcon(false)
    }

    private fun initViews() {
        setContentInsetsRelative(0, contentInsetEnd)
        setContentInsetsAbsolute(0, contentInsetRight)

        mBinding.imgComToolbarNavIcon.setOnClickListener {
            mOnBackListener?.invoke()
        }

        mBinding.imgComToolbarIcon.setOnClickListener {
            mOnClickListener?.invoke()
        }

        mBinding.textComToolbarTitle.setOnClickListener {
            mOnClickListener?.invoke()
        }

        mBinding.textComToolbarSubtitle.setOnClickListener {
            mOnClickListener?.invoke()
        }

        mBinding.imgContentCreatorExpand.setOnClickListener {
            mOnClickListener?.invoke()
        }
    }

    var icon: String = ""
        set(value) {
            field = value
            mBinding.imgComToolbarIcon.loadImageCircle(value)
            mBinding.imgComToolbarIcon.showWithCondition(value.isNotEmpty())
        }

    var title: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarTitle.text = value
            mBinding.textComToolbarTitle.showWithCondition(value.isNotEmpty())
        }

    var subtitle: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarSubtitle.text = MethodChecker.fromHtml(value)
            mBinding.textComToolbarSubtitle.showWithCondition(value.isNotEmpty())
        }

    var navIcon: Int = 0
        set(value) {
            field = value
            mBinding.imgComToolbarNavIcon.setImage(newIconId = navIcon)
        }

    fun setCustomizeContentColor(contentColor: ContentColor, enableDarkMode: Boolean = true) = with(mBinding) {
        val content: Int
        val contentAccent: Int
        val background: Int
        when(contentColor) {
            LIGHT, TRANSPARENT -> {
                content = if (enableDarkMode) getColor(context, Unify_NN0) else getColor(context, Unify_Static_White)
                contentAccent = if (enableDarkMode) getColor(context, Unify_NN400) else getColor(context, content_dms_soft_gray)
                background = if (contentColor == LIGHT) {
                    if (enableDarkMode) getColor(context, Unify_NN1000) else getColor(context, Unify_Static_Black)
                }
                else Color.TRANSPARENT
            }
            DARK -> {
                content = if (enableDarkMode) getColor(context, Unify_NN1000) else getColor(context, Unify_Static_Black)
                contentAccent = if (enableDarkMode) getColor(context, Unify_NN950) else getColor(context, content_dms_soft_dark)
                background = if (enableDarkMode) getColor(context, Unify_NN0) else getColor(context, Unify_Static_White)
            }
        }
        textComToolbarTitle.setTextColor(contentAccent)
        textComToolbarSubtitle.setTextColor(content)
        imgComToolbarNavIcon.setImage(newLightEnable = content, newDarkEnable = content)
        imgContentCreatorExpand.setImage(newLightEnable = content, newDarkEnable = content)
        toolbarParent.setBackgroundColor(background)
    }

    fun getToolbarParentView(): ConstraintLayout {
        return mBinding.toolbarParent
    }

    fun showHideExpandIcon(shouldShowExpandIcon: Boolean){
        mBinding.imgContentCreatorExpand.showWithCondition(shouldShowExpandIcon)
    }

    fun setOnBackClickListener(listener: AccountClickListener?) {
        mOnBackListener = listener
    }

    fun setOnAccountClickListener(listener: BackClickListener?) {
        mOnClickListener = listener

        showHideExpandIcon(mOnClickListener != null)
    }

    fun showCoachMarkSwitchAccount() {
        if (coachMark != null && coachMark?.isShowing == true) return
        getToolbarParentView().addOneTimeGlobalLayoutListener {
            coachMark = CoachMark2(context)
            coachMark?.showCoachMark(
                arrayListOf(
                    CoachMark2Item(
                        mBinding.textComToolbarSubtitle,
                        context.getString(contentCommonR.string.sa_coach_mark_title),
                        context.getString(contentCommonR.string.sa_coach_mark_subtitle),
                        CoachMark2.POSITION_BOTTOM
                    )
                )
            )
        }
    }

    fun hideCoachMarkSwitchAccount() {
        if (coachMark != null && coachMark?.isShowing == true) coachMark?.dismissCoachMark()
    }

}

typealias AccountClickListener = () -> Unit
typealias BackClickListener = () -> Unit

enum class ContentColor {
    LIGHT, DARK, TRANSPARENT
}
