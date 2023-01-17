package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroSetupTitleBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by fachrizalmrsln on 10/01/23
 */
class PlayBroadcastSetupTitleBottomSheet: BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroSetupTitleBinding? = null
    private val binding: BottomSheetPlayBroSetupTitleBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private lateinit var mTitle: String
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
        setChild(binding.root)
    }

    private fun setupView() = with(binding) {
        setTitle(getString(R.string.play_bro_title_label_bottom_sheet))

        tvSetupTitleField.apply {
            setCounter(mMaxCharacter)
            editText.setText(mTitle)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.isBlank() == true) return
                    if (mErrorState) {
                        mErrorState = false
                        isInputError = mErrorState
                        setMessage("")
                    }
                    btnSetupTitle.isEnabled = (p0 != null && !p0.contentEquals(mTitle))
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p1 == 0 && p0?.isBlank() == true) {
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

    fun setupTitle(title: String) {
        mTitle = title
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

    fun setMaxCharacter(maxCharacter: Int) {
        mMaxCharacter = maxCharacter
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnSetupTitle.isLoading = isLoading
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
        fun submitTitle(title: String)
    }

}
