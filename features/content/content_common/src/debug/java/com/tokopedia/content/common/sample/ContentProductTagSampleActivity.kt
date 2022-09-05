package com.tokopedia.content.common.sample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.databinding.ActivityContentProductTagSampleBinding
import com.tokopedia.content.common.di.DaggerContentProductTagSampleComponent
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
class ContentProductTagSampleActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var binding: ActivityContentProductTagSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        binding = ActivityContentProductTagSampleBinding.inflate(
            layoutInflater, null, false
        )
        setContentView(binding.root)

        setupDefault()
        setupListener()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when(fragment) {
            is ProductTagParentFragment -> {
                fragment.setListener(object : ProductTagParentFragment.Listener {
                    override fun onCloseProductTag() {
                        closeFragment()
                    }

                    override fun onFinishProductTag(products: List<SelectedProductUiModel>) {
                        Log.d("<LOG>", products.toString())
                        closeFragment()
                    }
                })

                fragment.setDataSource(object : ProductTagParentFragment.DataSource {
                    override fun getInitialSelectedProduct(): List<SelectedProductUiModel> {
                        return listOf(
                            SelectedProductUiModel.createOnlyId("2148279610"),
                            SelectedProductUiModel.createOnlyId("4207525260"),
                            SelectedProductUiModel.createOnlyId("2653580529"),
                        )
                    }
                })
            }
        }
    }

    private fun setupDefault() {
        binding.rbUser.isChecked = true
        binding.rbMultipleSelectionProductNo.isChecked = true
        binding.rbFullPageAutocompleteNo.isChecked = true
    }

    private fun setupListener() {
        binding.btnOpen.setOnClickListener {
            setupFragment()
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                getFragment(),
                ProductTagParentFragment.TAG
            )
            .commit()
    }

    private fun getFragment(): Fragment {
        return ProductTagParentFragment.getFragment(
            supportFragmentManager,
            classLoader,
            ContentProductTagArgument.Builder()
                .setShopBadge("")
                .setAuthorId(getAuthorId())
                .setAuthorType(getAuthorType())
                .setProductTagSource("global_search,own_shop,last_purchase")
                .setMultipleSelectionProduct(isMultipleSelectionProduct(), if(isMultipleSelectionProduct()) 3 else 0)
                .setFullPageAutocomplete(binding.rbFullPageAutocompleteYes.isChecked)
                .setBackButton(ContentProductTagConfig.BackButton.Close)
                .setIsShowActionBarDivider(false)
        )
    }

    private fun closeFragment() {
        supportFragmentManager.beginTransaction().remove(getFragment()).commit()
    }

    private fun getAuthorId(): String {
        return when(binding.rgOpenAs.checkedRadioButtonId) {
            binding.rbUser.id -> userSession.userId
            binding.rbSeller.id -> userSession.shopId
            else -> ""
        }
    }

    private fun getAuthorType(): String {
        return when(binding.rgOpenAs.checkedRadioButtonId) {
            binding.rbUser.id -> ContentCommonUserType.TYPE_USER
            binding.rbSeller.id -> ContentCommonUserType.TYPE_SHOP
            else -> ""
        }
    }

    private fun isMultipleSelectionProduct(): Boolean {
        return binding.rbMultipleSelectionProductYes.isChecked
    }

    private fun inject() {
        DaggerContentProductTagSampleComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onBackPressed() {
        ProductTagParentFragment.findFragment(supportFragmentManager)?.let {
            it.onBackPressed()
        } ?: super.onBackPressed()
    }
}