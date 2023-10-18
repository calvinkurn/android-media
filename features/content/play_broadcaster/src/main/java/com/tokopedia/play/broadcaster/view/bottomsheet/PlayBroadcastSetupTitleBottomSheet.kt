package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroSetupTitleBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 10/01/23
 */
class PlayBroadcastSetupTitleBottomSheet @Inject constructor(): BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroSetupTitleBinding? = null
    private val binding: BottomSheetPlayBroSetupTitleBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private var mTitle: String = ""
    private var mErrorState = false
    private var mMaxCharacter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroSetupTitleBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        setCloseClickListener {
            if (!binding.btnSetupTitle.isLoading)
                dismiss()
        }

        setChild(binding.root)
    }

    private fun setupView() = with(binding) {
        mListener?.onTitleFormOpen()
        setTitle(getString(R.string.play_bro_title_label_bottom_sheet))
        setOnDismissListener {
            mListener?.onBackPressedTitleForm()
            dismiss()
        }

        tvSetupTitleField.apply {
            addOnFocusChangeListener = { _, hasFocus ->
                if (hasFocus) mListener?.onTextFieldTitleFormClicked()
            }
            setCounter(mMaxCharacter)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.setText(mTitle)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if (mErrorState) {
                        mErrorState = false
                        isInputError = mErrorState
                        setMessage("")
                    }
                    btnSetupTitle.isEnabled = ((text != null && text.isNotBlank()) && !text.contentEquals(mTitle))
                }

                override fun onTextChanged(text: CharSequence?, textLength: Int, p2: Int, p3: Int) {
                    if (textLength == 0 && text?.isBlank() == true) {
                        editText.text.clear()
                        return
                    }
                }
            })
            editText.setOnEditorActionListener { _, _, _ ->
                hideSetupTitleKeyboard()
                return@setOnEditorActionListener false
            }
            clearIconView.setOnClickListener {
                mListener?.onTextFieldTitleFormCleared()
                editText.text.clear()
                btnSetupTitle.isEnabled = false
            }
        }
        btnSetupTitle.setOnClickListener {
            hideSetupTitleKeyboard()

            mListener?.submitTitle(tvSetupTitleField.editText.text.trim().toString())
            setLoading(true)
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    fun setupListener(listener: Listener) {
        mListener = listener
    }

    fun setupData(title: String, maxCharacter: Int) {
        mTitle = title
        mMaxCharacter = maxCharacter
    }

    fun successSubmit() {
        dismiss()
    }

    fun failSubmit(errorMessage: String?) {
        mErrorState = true

        setLoading(false)
        binding.tvSetupTitleField.isInputError = mErrorState
        binding.tvSetupTitleField.setMessage(errorMessage ?: getString(R.string.play_bro_default_error_message))
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnSetupTitle.isLoading = isLoading
        isCancelable = !isLoading
    }

    private fun hideSetupTitleKeyboard() {
        binding.tvSetupTitleField.editText.apply {
            requestFocus()
            hideKeyboard()
        }
    }

    companion object {
        const val TAG = "PlayBroadcastSetupTitleBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroadcastSetupTitleBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroadcastSetupTitleBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroadcastSetupTitleBottomSheet::class.java.name
            ) as PlayBroadcastSetupTitleBottomSheet
        }
    }

    interface Listener {
        fun onTitleFormOpen() {}
        fun onBackPressedTitleForm() {}
        fun onTextFieldTitleFormClicked() {}
        fun onTextFieldTitleFormCleared() {}
        fun submitTitle(title: String)
    }

}
