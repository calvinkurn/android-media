package com.tokopedia.linter


enum class Priority(var value: Int) {
    Low(1),
    Medium(5),
    High(10)
}

sealed class LinterConstants {

    object GradleConstructs : LinterConstants() {
        const val DEPENDENCIES = "dependencies"
        const val IMPLEMENTATION = "implementation"
    }

    object Libraries : LinterConstants() {
        const val KOTLIN_REFLECTION = "rootProject.ext.supportLibDependencies.kotlinReflection"
        const val TKPD_DESIGN = "tkpddesign"
        const val TKPD_DESIGN_LIBRARIES = "rootProject.ext.libraries.tkpddesign"
    }

    object Classes : LinterConstants() {
        const val BOTTOM_SHEET_DIALOG = "com.google.android.material.bottomsheet.BottomSheetDialog"
        const val DIALOG_CLASS_NAME = "android.app.Dialog"
        const val DATE_PICKER = "android.widget.DatePicker"
        const val FLOATING_BUTTON = "com.tokopedia.design.component.FloatingButton"
        const val FLOATING_ACTION_BUTTON = "com.google.android.material.floatingactionbutton.FloatingActionButton"
        const val SEARCH_BAR_VIEW = "com.tokopedia.autocomplete.searchbar.SearchBarView"
        const val SNACK_BAR = "com.google.android.material.snackbar.Snackbar"
        const val CORE_RESOURCES = "com.tokopedia.core.R"
    }

    object UnifyClasses : LinterConstants() {
        const val TYPOGRAPHY = "com.tokopedia.unifyprinciples.Typography"
        const val TOASTER = "com.tokopedia.unifycomponents.Toaster"
        const val DIALOG = "com.tokopedia.dialog.DialogUnify"
        const val DATE_PICKER_UNIFY = "com.tokopedia.datepicker.DatePickerUnify"
        const val DATE_PICKER = "com.tokopedia.datepicker.DatePicker"
        const val FLOATING_BUTTON = "com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify"
        const val IMAGE_BUTTON = "com.tokopedia.unifycomponents.UnifyImageButton"
        const val BUTTON = "com.tokopedia.unifycomponents.UnifyButton"
        const val TAB = "com.tokopedia.unifycomponents.TabsUnify"
        const val SEARCH_BAR = "com.tokopedia.unifycomponents.SearchBarUnify"
        const val PROGRESS_BAR = "com.tokopedia.unifycomponents.LoaderUnify"
        const val BOTTOM_SHEET = "com.tokopedia.unifycomponents.BottomSheetUnify"
    }


}


