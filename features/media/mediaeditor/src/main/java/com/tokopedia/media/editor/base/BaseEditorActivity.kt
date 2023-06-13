package com.tokopedia.media.editor.base

import android.R.id.home
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.media.editor.ui.EditorFragmentProvider
import com.tokopedia.media.editor.ui.EditorFragmentProviderImpl

abstract class BaseEditorActivity : BaseSimpleActivity() {
    abstract fun initViewModel()
    abstract fun initBundle(savedInstanceState: Bundle?)
    abstract fun initInjector()
    abstract fun onHeaderActionClick()
    abstract fun onBackClicked()

    private val unifyToolbar: HeaderUnify by lazy {
        HeaderUnify(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initBundle(savedInstanceState)
    }

    protected open fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

    fun setHeader(title: CharSequence, actionText: CharSequence? = null, rightIcon: Int? = null) {
        clearOldToolbar()

        setSupportActionBar(unifyToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        unifyToolbar.title = title
        unifyToolbar.isShowBackButton = true

        if (!actionText.isNullOrEmpty()) {
            unifyToolbar.actionText = actionText
            unifyToolbar.actionTextView?.setOnClickListener {
                onHeaderActionClick()
            }
        } else if (rightIcon != null) {
            unifyToolbar.addRightIconNotification(rightIcon).apply {
                setOnClickListener {
                    onHeaderActionClick()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == home) {
            onBackClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearOldToolbar() {
        val parent = toolbar.parent as ViewGroup
        parent.removeView(toolbar)
        parent.addView(unifyToolbar, 0)
    }
}
