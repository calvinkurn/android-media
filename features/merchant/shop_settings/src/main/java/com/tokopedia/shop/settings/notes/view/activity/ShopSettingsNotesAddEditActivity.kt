package com.tokopedia.shop.settings.notes.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.databinding.ActivityShopSettingsAddNewBinding
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesAddEditFragment

class ShopSettingsNotesAddEditActivity: BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {

    private var binding : ActivityShopSettingsAddNewBinding? = null
    private var isEdit = false
    private var isReturnablePolicy = false
    private var shopNote = ShopNoteUiModel()

    companion object {
        private const val PARAM_IS_RETURNABLE_POLICY = "IS_RETURNABLE_POLICY"
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_NOTE = "SHOP_NOTE"

        @JvmStatic
        fun createIntent(context: Context, isReturnablePolicy: Boolean, isEdit: Boolean, shopNoteModel: ShopNoteUiModel = ShopNoteUiModel()) =
                Intent(context, ShopSettingsNotesAddEditActivity::class.java)
                        .putExtra(PARAM_SHOP_NOTE, shopNoteModel)
                        .putExtra(PARAM_IS_RETURNABLE_POLICY, isReturnablePolicy)
                        .putExtra(PARAM_IS_EDIT, isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopNote = intent.getParcelableExtra(PARAM_SHOP_NOTE) ?: ShopNoteUiModel()
        isEdit = intent.getBooleanExtra(PARAM_IS_EDIT, false)
        isReturnablePolicy = intent.getBooleanExtra(PARAM_IS_RETURNABLE_POLICY, false)

        super.onCreate(savedInstanceState)

        setupToolbar()

        supportActionBar?.setTitle(if (!isEdit) R.string.shop_settings_add_note else R.string.shop_settings_edit_note)
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun getNewFragment() = ShopSettingsNotesAddEditFragment.createInstance(isReturnablePolicy, isEdit, shopNote)

    override fun getLayoutRes() = R.layout.activity_shop_settings_add_new

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getComponent(): ShopSettingsComponent = DaggerShopSettingsComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityShopSettingsAddNewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun setupToolbar() {
        binding?.header?.apply {
            isShowShadow = true
            setSupportActionBar(this)
            actionTextView?.apply {
                setOnClickListener { (fragment as ShopSettingsNotesAddEditFragment).saveAddEditNote() }
            }
        }
    }
}