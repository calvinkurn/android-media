package com.tokopedia.shop.settings.notes.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesAddEditFragment

class ShopSettingNotesAddEditActivity: BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {
    private var isEdit = false
    private var isReturnablePolicy = false
    private var shopNote = ShopNoteViewModel()

    private val saveTextView: TextView? by lazy {
        toolbar?.findViewById<TextView>(R.id.tvSave)
    }

    companion object {
        private const val PARAM_IS_RETURNABLE_POLICY = "IS_RETURNABLE_POLICY"
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_NOTE = "SHOP_NOTE"

        @JvmStatic
        fun createIntent(context: Context, isReturnablePolicy: Boolean, isEdit: Boolean, shopNoteModel: ShopNoteViewModel = ShopNoteViewModel()) =
                Intent(context, ShopSettingNotesAddEditActivity::class.java)
                        .putExtra(PARAM_SHOP_NOTE, shopNoteModel)
                        .putExtra(PARAM_IS_RETURNABLE_POLICY, isReturnablePolicy)
                        .putExtra(PARAM_IS_EDIT, isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopNote = intent.getParcelableExtra(PARAM_SHOP_NOTE) ?: ShopNoteViewModel()
        isEdit = intent.getBooleanExtra(PARAM_IS_EDIT, false)
        isReturnablePolicy = intent.getBooleanExtra(PARAM_IS_RETURNABLE_POLICY, false)

        super.onCreate(savedInstanceState)

        saveTextView?.run {
            setOnClickListener { (fragment as ShopSettingsNotesAddEditFragment).saveAddEditNote() }
            visibility = View.VISIBLE
        }

        supportActionBar?.setTitle(if (!isEdit) R.string.shop_settings_add_note else R.string.shop_settings_edit_note)
    }

    override fun getNewFragment() = ShopSettingsNotesAddEditFragment.createInstance(isReturnablePolicy, isEdit, shopNote)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new

    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.title = this.title
        }
    }
}