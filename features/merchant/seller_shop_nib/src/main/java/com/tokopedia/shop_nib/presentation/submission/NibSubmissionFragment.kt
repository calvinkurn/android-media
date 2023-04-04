package com.tokopedia.shop_nib.presentation.submission

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionBinding
import com.tokopedia.shop_nib.presentation.di.component.DaggerShopNibComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class NibSubmissionFragment : BaseDaggerFragment() {

    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 100

        @JvmStatic
        fun newInstance(): NibSubmissionFragment {
            return NibSubmissionFragment()
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

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
        binding?.layoutFilePicker?.setOnClickListener {
            showFilePicker()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val uri = data.data
                val filePath = getRealPathFromURI( uri ?: return)
                val result = MediaPicker.result(data)
                print(result)
            }
        }
    }
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? = context?.contentResolver?.query(contentURI, null, null, null, null)
        return if (cursor == null) { // Source is Dropbox or other similar local file path
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }
    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val allowedFileTypes = arrayOf("image/png", "image/jpeg", "application/pdf")
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, allowedFileTypes)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
    }

    /*private fun showFilePicker() {
        val pintent = MediaPicker.intent(activity ?: return){
            modeType(ModeType.IMAGE_ONLY)
            singleSelectionMode()
            maxImageFileSize(5_000_000) //5MB
        }
        startActivityForResult(pintent, REQUEST_CODE_SELECT_FILE)
    }*/

}
