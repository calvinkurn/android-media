package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentManageLocationLayoutBinding
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId

class ManageLocationFragment : BaseMultiFragment() {

    companion object {

        private const val BUNDLE_KEY_NEGATIVE_CASE_ID = "negativeCaseId"

        // image resource url
        const val IMG_STATIC_URI_NO_PIN_POIN = "https://images.tokopedia.net/img/ic-tokofood_home_no_pin_poin.png"
        const val IMG_STATIC_URI_NO_ADDRESS = "https://images.tokopedia.net/img/ic_tokofood_home_no_address.png"
        const val IMG_STATIC_URI_OUT_OF_COVERAGE = "https://images.tokopedia.net/img/ic_tokofood_home_out_of_coverage.png"

        // negative case ids
        const val EMPTY_STATE_OUT_OF_COVERAGE = "2"
        const val EMPTY_STATE_NO_PIN_POINT = "3"
        const val EMPTY_STATE_NO_ADDRESS = "4"

        @JvmStatic
        fun createInstance(negativeCaseId: String = "") = ManageLocationFragment().apply {
            this.arguments = Bundle().apply {
                putString(BUNDLE_KEY_NEGATIVE_CASE_ID, negativeCaseId)
            }
        }
    }

    private var binding: FragmentManageLocationLayoutBinding? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = FragmentManageLocationLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        context?.run {
            when(arguments?.getString(BUNDLE_KEY_NEGATIVE_CASE_ID) ?: "") {
                TokoFoodHomeStaticLayoutId.EMPTY_STATE_NO_PIN_POINT -> bindNoPinPoint(this)
                TokoFoodHomeStaticLayoutId.EMPTY_STATE_NO_ADDRESS -> bindNoAddress(this)
                TokoFoodHomeStaticLayoutId.EMPTY_STATE_OUT_OF_COVERAGE -> bindOutOfCoverage(this)
            }
        }
    }

    private fun bindNoPinPoint(context: Context) {
        val title = context.getString(R.string.home_no_pin_poin_title)
        val desc = context.getString(R.string.home_no_pin_poin_desc)
        binding?.tpgLocationBsToHome?.hide()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_NO_PIN_POIN)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_no_pin_poin_button)
        binding?.btnLocationBsCta?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.GEOLOCATION)
        }
    }

    private fun bindNoAddress(context: Context) {
        val title = context.getString(R.string.home_no_address_title)
        val desc = context.getString(R.string.home_no_address_desc)
        binding?.tpgLocationBsToHome?.show()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_NO_ADDRESS)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_no_address_button)
        binding?.btnLocationBsCta?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
        }
        binding?.tpgLocationBsToHome?.setOnClickListener {

        }
    }

    private fun bindOutOfCoverage(context: Context) {
        val title = context.getString(R.string.home_out_of_coverage_title)
        val desc = context.getString(R.string.home_out_of_coverage_desc)
        binding?.tpgLocationBsToHome?.show()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_OUT_OF_COVERAGE)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_out_of_coverafe_button)
        binding?.btnLocationBsCta?.setOnClickListener {

        }
        binding?.tpgLocationBsToHome?.setOnClickListener {

        }
    }
}