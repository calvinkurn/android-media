package com.tokopedia.shop_nib.presentation.submission

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionBinding
import com.tokopedia.shop_nib.di.component.DaggerShopNibComponent
import com.tokopedia.shop_nib.util.FileHelper
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.shop_nib.R
import com.tokopedia.shop_nib.util.extension.toMb
import java.io.File

class NibSubmissionFragment : BaseDaggerFragment() {

    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 100
        private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 //5 MB

        @JvmStatic
        fun newInstance(): NibSubmissionFragment {
            return NibSubmissionFragment()
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    @Inject
    lateinit var fileHelper: FileHelper

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[NibSubmissionViewModel::class.java] }
    private var binding by autoClearedNullable<SsnFragmentNibSubmissionBinding>()

    override fun getScreenName(): String = NibSubmissionFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopNibComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsnFragmentNibSubmissionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.layoutFilePickerDefault?.setOnClickListener {
            showFilePicker()
        }
        binding?.iconClose?.setOnClickListener { removeSelectedFile() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                showSelectedFile(data)
            }
        }
    }


    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg,image/png,application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/png", "image/jpeg", "application/pdf"))
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)

        try {
            startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
        } catch (_: ActivityNotFoundException) {}

    }

    private fun showSelectedFile(intent: Intent) {
        val fileUri = intent.data ?: return

        val fileName = fileHelper.getFileName(fileUri)
        val fileExtension = fileHelper.getFileExtension(fileUri)
        val fileSizeInMb = fileHelper.getFileSizeInBytes(fileUri).toMb()

        val isPdf = fileExtension == "pdf"

        binding?.run {
            if (isPdf) {
                val bitmap = fileHelper.generatePdfThumbnail(fileUri)
                imgFile.loadImage(bitmap)
            } else {
                imgFile.loadImage(fileUri)
            }

            layoutFilePickerDefault.gone()
            layoutFilePickerSelected.visible()
            tpgFileName.text = fileName

            val roundedFileSizeInMb = String.format("%.2f", fileSizeInMb)
            tpgFileSize.text = context?.getString(R.string.ssn_placeholder_file_size, roundedFileSizeInMb)
        }
    }

    private fun removeSelectedFile() {
        binding?.run {
            layoutFilePickerDefault.visible()
            layoutFilePickerSelected.gone()
        }
    }


}
