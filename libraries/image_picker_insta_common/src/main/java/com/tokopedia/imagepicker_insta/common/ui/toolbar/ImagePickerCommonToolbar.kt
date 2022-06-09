package com.tokopedia.imagepicker_insta.common.ui.toolbar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker_insta.common.R
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.Typography

class ImagePickerCommonToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.Toolbar(context, attrs) {

    private lateinit var toolbarIcon: AppCompatImageView
    private lateinit var toolbarTitle: Typography
    private lateinit var toolbarSubtitle: Typography
    private lateinit var toolbarNavIcon: AppCompatImageView
    private lateinit var toolbarExpandIcon: IconUnify
    private lateinit var toolbarParent: ConstraintLayout

    private fun getLayout() = R.layout.imagepicker_insta_com_toolbar

    private var mOnClickListener: AccountClickListener? = null
    private var mOnBackListener: BackClickListener? = null

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        setContentInsetsRelative(0,contentInsetEnd)
        setContentInsetsAbsolute(0,contentInsetRight)

        background = ColorDrawable(MethodChecker.getColor(context,R.color.Unify_Background))
        toolbarIcon  = findViewById(R.id.img_com_toolbar_icon)
        toolbarTitle = findViewById(R.id.img_com_toolbar_title)
        toolbarSubtitle = findViewById(R.id.img_com_toolbar_subtitle)
        toolbarNavIcon  = findViewById(R.id.img_com_toolbar_nav_icon)
        toolbarParent  = findViewById(R.id.toolbar_parent)
        toolbarExpandIcon  = findViewById(R.id.content_creator_expand_icon)

        toolbarNavIcon.setOnClickListener {
            mOnBackListener?.invoke()
        }

    }

    var icon: String = ""
        set(value) {
            field = value

            toolbarIcon.loadImageCircle(value)
            toolbarIcon.visibility = if(value.isNotEmpty()) View.VISIBLE else View.GONE
        }

    var title: String = ""
        set(value) {
            field = value
            toolbarTitle.text = value
        }

    var subtitle: String = ""
        set(value) {
            field = value
            toolbarSubtitle.text = value
        }

    fun getToolbarParentView(): ConstraintLayout {
        return toolbarParent
    }
    fun showHideExpandIcon(shouldShowExpandIcon: Boolean){
        toolbarExpandIcon.visibility = if(shouldShowExpandIcon) View.VISIBLE else View.GONE
    }
    fun setClickListenerToOpenBottomSheet(){
        toolbarExpandIcon.setOnClickListener {
            mOnClickListener?.invoke()
        }

        toolbarSubtitle.setOnClickListener {
            mOnClickListener?.invoke()
        }

    }

    fun setOnBackClickListener(listener: AccountClickListener?) {
        mOnBackListener = listener
    }

    fun setOnAccountClickListener(listener: BackClickListener?) {
        mOnClickListener = listener

        showHideExpandIcon(mOnClickListener != null)
    }
}

typealias AccountClickListener = () -> Unit
typealias BackClickListener = () -> Unit