package com.tokopedia.updateinactivephone.features.submitnewphone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.MINIMUM_PHONE_NUMBER
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneDataUploadBinding
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.File
import javax.inject.Inject

abstract class BaseInactivePhoneSubmitDataFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneDataUploadViewModel::class.java) }

    @Inject
    lateinit var trackerRegular: InactivePhoneTracker

    @Inject
    lateinit var trackerWithPin: InactivePhoneWithPinTracker

    var viewBinding by autoClearedNullable<FragmentInactivePhoneDataUploadBinding>()
    var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    protected abstract fun initObserver()
    protected abstract fun onSubmit()
    protected abstract fun dialogOnBackPressed()
    protected abstract fun initView()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.application?.let {
            InactivePhoneComponentBuilder
                .getComponent(it)
                .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentInactivePhoneDataUploadBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, unifyR.color.Unify_Background))
        }
        inactivePhoneUserDataModel = arguments?.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()

        viewBinding?.buttonNext?.setOnClickListener {
            onSubmit()
        }
    }

    open fun gotoSuccessPage(source: String) {
        removeFiles()

        activity?.let {
            val intent = InactivePhoneSuccessPageActivity.createIntent(it, source, inactivePhoneUserDataModel)
            startActivity(intent)
            it.finish()
        }
    }

    fun isPhoneValid(): Boolean {
        val phoneNumber = viewBinding?.textPhoneNumber?.text.toString()
        when {
            phoneNumber.isEmpty() -> {
                viewBinding?.textPhoneNumber?.error = getString(R.string.text_form_error_empty)
                return false
            }
            phoneNumber.length < MINIMUM_PHONE_NUMBER -> {
                viewBinding?.textPhoneNumber?.error = getString(R.string.text_form_error_min_9_digit)
                return false
            }
        }

        viewBinding?.textPhoneNumber?.clearErrorMessage()
        return true
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(viewBinding?.textPhoneNumber?.windowToken, 0)
    }

    fun showThumbnailLayout() {
        viewBinding?.layoutThumbnailDataUpload?.show()
    }

    fun hideThumbnailLayout() {
        viewBinding?.layoutThumbnailDataUpload?.hide()
    }

    open fun showLoading() {
        viewBinding?.loader?.show()
    }

    open fun hideLoading() {
        viewBinding?.loader?.hide()
    }

    fun removeFiles() {
        val inactivePhoneDirectory = context?.let {
            InactivePhoneConstant.getInternalDirectory(it)
        }?.let { File(it) }

        if (inactivePhoneDirectory?.exists() == true) {
            inactivePhoneDirectory.list()?.forEach {
                File(inactivePhoneDirectory, it).delete()
            }
        }
    }
}
