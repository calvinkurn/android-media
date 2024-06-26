package com.tokopedia.media.editor.ui.activity.addtext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.fragment.AddTextFragment
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import javax.inject.Inject

class AddTextActivity : BaseEditorActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private val viewModel: AddTextViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(AddTextViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun initViewModel() {}

    override fun initBundle(savedInstanceState: Bundle?) {
        intent.getParcelableExtra<EditorDetailUiModel>(ADD_TEXT_PARAM)?.apply {
            this.resultUrl?.let { viewModel.setImageUrl(it) }
            this.addTextValue?.let { viewModel.textData = it }
        }

        intent.getIntExtra(ADD_TEXT_MODE, -1).apply {
            viewModel.setPageMode(this)
            var headerTitle = HEADER_TITLE_ADD_TEXT

            if (this == TEXT_MODE) {
                initObserverInput()
            } else {
                headerTitle = HEADER_TITLE_MOVE_POSITION
                hideHeaderAction()
            }

            setHeader(headerTitle, HEADER_ACTION)
        }
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    override fun onHeaderActionClick() {
        finishPage()
    }

    override fun onBackClicked() {}

    override fun getNewFragment(): Fragment {
        return fragmentProvider().addTextFragment()
    }

    override fun onStart() {
        super.onStart()
        hideHeaderAction()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.textInput.value.isNullOrEmpty() && viewModel.pageMode.value == TEXT_MODE) {
            showHeaderAction()
        }
    }

    private fun initObserverInput() {
        viewModel.textInput.observe(this) {
            if (it.isNotEmpty()) {
                showHeaderAction()
            } else {
                hideHeaderAction()
            }
        }
    }

    fun finishPage() {
        val intent = Intent()
        try {
            val result = (fragment as AddTextFragment).getInputResult()
            intent.putExtra(ADD_TEXT_RESULT, result)
            setResult(Activity.RESULT_OK, intent)
        } catch (_: Exception) {
            setResult(Activity.RESULT_CANCELED, intent)
        }

        finish()
    }

    companion object {
        const val ADD_TEXT_PARAM = "intent_data.add_text"
        const val ADD_TEXT_RESULT = "result_data.add_text"

        // flag to decide showing textbox mode / change position mode
        const val ADD_TEXT_MODE = "mode_data.add_text"

        const val ADD_TEXT_REQUEST_CODE = 389

        private const val HEADER_TITLE_ADD_TEXT = "Tambah Teks"
        private const val HEADER_TITLE_MOVE_POSITION = "Ubah Posisi Teks"
        private const val HEADER_ACTION = "Simpan"

        const val TEXT_MODE = 0
        const val POSITION_MODE = 1
    }
}
