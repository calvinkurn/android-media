package com.tokopedia.content.common.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.databinding.ActivityContentProductTagSampleBinding
import com.tokopedia.content.common.di.DaggerContentProductTagSampleComponent
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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

    @Inject
    lateinit var mAnalytic: ContentProductTagAnalytic

    private var _binding: ActivityContentProductTagSampleBinding? = null
    private val binding: ActivityContentProductTagSampleBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        _binding = ActivityContentProductTagSampleBinding.inflate(
            layoutInflater, null, false
        )
        setContentView(binding.root)

        setupListener()
        setupDefault()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when(fragment) {
            is ContentProductTagSampleBottomSheet -> {
                fragment.setDataSource(object : ContentProductTagSampleBottomSheet.DataSource {
                    override fun getProductTagArgumentBuilder(): ContentProductTagArgument.Builder {
                        return getArgumentBuilder()
                    }
                })
            }
            is ProductTagParentFragment -> {
                fragment.setListener(object : ProductTagParentFragment.Listener {
                    override fun onCloseProductTag() {
                        closeFragment()
                    }

                    override fun onFinishProductTag(products: List<SelectedProductUiModel>) {
                        Log.d("<LOG>", products.toString())
                        closeFragment()
                    }

                    override fun onMaxSelectedProductReached() {
                        Log.d("<LOG>", "Kamu udah pilih ${getMaxSelectedProduct()} produk.")
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

                fragment.setAnalytic(mAnalytic)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ProductTagParentFragment.findFragment(supportFragmentManager)?.onNewIntent(intent)
    }

    private fun setupDefault() {
        binding.rbUser.isChecked = true
        binding.rbMultipleSelectionProductYes.isChecked = true
        binding.rbFullPageAutocompleteNo.isChecked = true
        binding.textFieldMaxSelectedProduct.editText.setText("3")

        binding.cbxGlobalSearch.isChecked = true
        binding.cbxLastPurchased.isChecked = true
        binding.cbxMyShop.isChecked = true

        binding.cbxUseBottomSheet.isChecked = true

        binding.cbxIsAutoHandleBackPressed.isChecked = true
    }

    private fun setupListener() {
        binding.btnOpen.setOnClickListener {
            hideKeyboard()
            binding.textFieldMaxSelectedProduct.clearFocus()

            if(validate()) {
                setupProductPicker()
            }
        }

        binding.rgMultipleSelectionProduct.setOnCheckedChangeListener { radioGroup, i ->
            binding.textFieldMaxSelectedProduct.showWithCondition(
                binding.rgMultipleSelectionProduct.checkedRadioButtonId == binding.rbMultipleSelectionProductYes.id
            )
        }
    }

    private fun setupProductPicker() {
        if(isUseBottomSheet()) {
            ContentProductTagSampleBottomSheet.getFragment(
                supportFragmentManager,
                classLoader,
            ).showNow(supportFragmentManager)
        }
        else {
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.fragmentContainer.id,
                    getFragment(),
                    ProductTagParentFragment.TAG,
                )
                .commit()
        }
    }

    private fun getFragment(): Fragment {
        return ProductTagParentFragment.getFragment(
            supportFragmentManager,
            classLoader,
            getArgumentBuilder()
        )
    }

    private fun getArgumentBuilder(): ContentProductTagArgument.Builder {
        return ContentProductTagArgument.Builder()
            .setShopBadge("")
            .setAuthorId(getAuthorId())
            .setAuthorType(getAuthorType())
            .setProductTagSource(getProductTagSource())
            .setMultipleSelectionProduct(isMultipleSelectionProduct(), getMaxSelectedProduct())
            .setFullPageAutocomplete(binding.rbFullPageAutocompleteYes.isChecked, getApplinkAfterAutocomplete())
            .setBackButton(ContentProductTagConfig.BackButton.Close)
            .setIsShowActionBarDivider(false)
            .setIsAutoHandleBackPressed(getIsAutoHandleBackPressed())

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

    private fun getProductTagSource(): String{
        return mutableListOf<String>().apply {
            if(binding.cbxGlobalSearch.isChecked) {
                add("global_search")
            }

            if(binding.cbxLastPurchased.isChecked) {
                add("last_purchase")
            }

            if(binding.cbxMyShop.isChecked) {
                add("own_shop")
            }
        }.joinToString(separator = ",")
    }

    private fun isMultipleSelectionProduct(): Boolean {
        return binding.rbMultipleSelectionProductYes.isChecked
    }

    private fun getMaxSelectedProduct(): Int {
        return if(isMultipleSelectionProduct())
            binding.textFieldMaxSelectedProduct.editText.text.toString().toIntOrZero()
        else 0
    }

    private fun isUseBottomSheet(): Boolean {
        return binding.cbxUseBottomSheet.isChecked
    }

    private fun getIsAutoHandleBackPressed(): Boolean {
        return binding.cbxIsAutoHandleBackPressed.isChecked
    }

    private fun getApplinkAfterAutocomplete(): String {
        return "tokopedia-android-internal://sample/content-creation-product-search"
    }

    private fun validate(): Boolean {
        return if(binding.rbMultipleSelectionProductYes.isChecked && binding.textFieldMaxSelectedProduct.editText.text.isEmpty()) {
            Toast.makeText(this, "Please input Max Selected Product", Toast.LENGTH_SHORT).show()
            false
        }
        else true
    }

    private fun inject() {
        DaggerContentProductTagSampleComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
