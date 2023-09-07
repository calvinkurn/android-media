package com.tokopedia.editor.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * To create the fragment in this universal-editor, you could follow this template below.
 *
 * class FooFragment @inject constructor() : BaseEditorFragment(R.layout.fragment_foo) {
 *
 *     private val binding: FragmentFooBinding? by viewBinding()
 *
 *     ...
 * }
 *
 * Don't forget to use a view-binding to maintain type safety of view object references.
 *
 */
abstract class BaseEditorFragment(layoutId: Int) : Fragment(layoutId) {

    /**
     * Any particular views (e.g. initial view state, set text, observe the click listener)
     * we could call it on [initView] method.
     */
    abstract fun initView()

    /**
     * The [initObserver] method will collecting all of observers that contains
     * in the specific pages. If we need to observe data from Presenter, please
     * call the observable on this method.
     */
    abstract fun initObserver()

    /**
     * [onLoadSavedState] used to load the temporary data retained state.
     *
     * We could get the temporary data from [bundle] parameter. This method is
     * delightful to use if we want to keep persistent of the state of the page.
     *
     * For example, if we have an UiModel, somehow if we switch the app to
     * another app, the system will destroyed inactive page within particular time,
     * hence, to maintain the data, we have to save the data through [onSaveInstanceState].
     *
     * And To get the data, we could get it through [onLoadSavedState] method.
     */
    protected open fun onLoadSavedState(bundle: Bundle?) = Unit

    open fun onLoadContent(path: String) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLoadSavedState(savedInstanceState)
        initObserver()
        initView()
    }

}
