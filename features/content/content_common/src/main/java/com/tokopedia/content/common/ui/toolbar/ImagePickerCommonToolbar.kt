package com.tokopedia.content.common.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.R.color.Unify_NN1000
import com.tokopedia.content.common.R.color.Unify_NN950
import com.tokopedia.content.common.R.color.soft_dark
import com.tokopedia.content.common.R.color.soft_gray
import com.tokopedia.content.common.databinding.ImagepickerInstaComToolbarBinding
import com.tokopedia.content.common.ui.toolbar.ContentColor.*
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.R.color.Unify_NN0
import com.tokopedia.unifyprinciples.R.color.Unify_NN400
import com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
import com.tokopedia.unifyprinciples.R.color.Unify_Static_White
import com.tokopedia.content.common.R as contentCommonR

class ImagePickerCommonToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : Toolbar(context, attrs) {

    private var mOnClickListener: AccountClickListener? = null
    private var mOnBackListener: BackClickListener? = null
    private var mBinding = ImagepickerInstaComToolbarBinding.inflate(LayoutInflater.from(context), this)
    private lateinit var coachMark: CoachMark2

    init {
        initViews()
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

        mBinding.textComToolbarSubtitle.setOnClickListener {
            mOnClickListener?.invoke()
        }
    }

    var icon: String = ""
        set(value) {
            field = value
            mBinding.imgComToolbarIcon.loadImageCircle(value)
            mBinding.imgComToolbarIcon.visibility = if(value.isNotEmpty()) View.VISIBLE else View.GONE
        }

    var title: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarTitle.text = value
            mBinding.textComToolbarTitle.visibility = if(value.isNotEmpty()) View.VISIBLE else View.GONE
        }

    var subtitle: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarSubtitle.text = value
            mBinding.textComToolbarSubtitle.visibility = if(value.isNotEmpty()) View.VISIBLE else View.GONE
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
                contentAccent = if (enableDarkMode) getColor(context, Unify_NN400) else getColor(context, soft_gray)
                background = if (contentColor == LIGHT) {
                    if (enableDarkMode) getColor(context, Unify_NN1000) else getColor(context, Unify_Static_Black)
                }
                else Color.TRANSPARENT
            }
            DARK -> {
                content = if (enableDarkMode) getColor(context, Unify_NN1000) else getColor(context, Unify_Static_Black)
                contentAccent = if (enableDarkMode) getColor(context, Unify_NN950) else getColor(context, soft_dark)
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
        mBinding.imgContentCreatorExpand.visibility = if(shouldShowExpandIcon) View.VISIBLE else View.GONE
    }

    fun setOnBackClickListener(listener: AccountClickListener?) {
        mOnBackListener = listener
    }

    fun setOnAccountClickListener(listener: BackClickListener?) {
        mOnClickListener = listener

        showHideExpandIcon(mOnClickListener != null)
    }

    fun showCoachMarkSwitchAccount() {
        getToolbarParentView().addOneTimeGlobalLayoutListener {
            coachMark = CoachMark2(context)
            coachMark.showCoachMark(
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
        if (::coachMark.isInitialized && coachMark.isShowing) coachMark.dismissCoachMark()
    }

}

typealias AccountClickListener = () -> Unit
typealias BackClickListener = () -> Unit

enum class ContentColor {
    LIGHT, DARK, TRANSPARENT
}