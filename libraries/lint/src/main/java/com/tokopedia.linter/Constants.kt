package com.tokopedia.linter


const val FORBIDDEN_IMPORT = "Forbidden import"
const val FIX_NAME_FORMAT = "Use %s instead!"
const val WARNING_DESCRIPTION_FORMAT = "%s should probably be %s"
const val WARNING_EXPLANATION_FORMAT = "Using a %s is not recommended, you should be using %s instead."
const val USAGE_ERROR_MESSAGE_FORMAT = "consider using %s."

// BottomSheetUnifyDetector
const val BOTTOM_SHEET_ISSUE_ID = "BottomSheetUnifyUsage"
const val BOTTOM_SHEET_DIALOG = "com.google.android.material.bottomsheet.BottomSheetDialog"
const val BOTTOM_SHEET_DIALOG_FRAGMENT = "com.google.android.material.bottomsheet.BottomSheetDialogFragment"
const val BOTTOM_SHEET_OLD_NAME = "BottomSheets"
const val BOTTOM_SHEET_NEW_NAME = "BottomSheetUnify"

// DatePickerUnifyDetector
const val DATE_PICKER_ISSUE_ID = "DatePickerUnifyUsage"
const val DATE_PICKER = "android.widget.DatePicker"
const val DATE_PICKER_DIALOG = "android.app.DatePickerDialog"
const val DATE_PICKER_OLD_NAME = "DatePicker"
const val DATE_PICKER_NEW_NAME = "DatePickerUnify"

// DialogUnifyDetector
const val DIALOG_ISSUE_ID = "DialogUnifyUsage"
const val DIALOG_CLASS_NAME = "android.app.Dialog"
const val DIALOG_OLD_NAME = "Dialog"
const val DIALOG_NEW_NAME = "DialogUnify"

// FloatingButtonUnifyDetector
const val FLOATING_BUTTON_ISSUE_ID = "FloatingButtonUnifyUsage"
const val FLOATING_BUTTON_ACTUAL_SHORT_NAME = "FloatingButton"
const val FLOATING_BUTTON_FIX_SHORT_NAME = "FloatingButtonUnify"
const val FLOATING_BUTTON_OLD_NAME = "com.tokopedia.design.component.FloatingButton"
const val FLOATING_ACTION_BUTTON_OLD_NAME = "com.google.android.material.floatingactionbutton.FloatingActionButton"
const val FLOATING_BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify"

// LoaderUnifyDetector
const val PROGRESSBAR_ISSUE_ID = "LoaderUnifyUsage"
const val PROGRESSBAR_OLD_NAME = "ProgressBar"
const val PROGRESSBAR_NEW_NAME = "com.tokopedia.unifycomponents.LoaderUnify"
const val PROGRESSBAR_NEW_NAME_SHORT = "LoaderUnify"

//SearchBarUnifyDetector
const val SEARCHBAR_ISSUE_ID = "SearchBarUnifyUsage"
const val SEARCHBAR_OLD_NAME = "SearchView"
const val SEARCHBARVIEW_OLD_NAME = "com.tokopedia.autocomplete.searchbar.SearchBarView"
const val SEARCHBAR_NEW_NAME = "com.tokopedia.unifycomponents.SearchBarUnify"

//TabsUnifyDetector
const val TABS_ISSUE_ID = "TabsUnifyUsage"
const val TABS_ACTUAL_SHORT_NAME = "Tabs"
const val TAB_LAYOUT_ACTUAL_SHORT_NAME = "Tabs"
const val TABS_FIX_SHORT_NAME = "TabsUnify"
const val TABS_OLD_NAME = "com.tokopedia.design.component.Tabs"
const val TAB_LAYOUT_OLD_NAME = "com.google.android.material.tabs.TabLayout"
const val TAB_NEW_NAME = "com.tokopedia.unifycomponents.TabsUnify"

// ToasterDetector
const val SNACKBAR_ISSUE_ID = "ToasterUsage"
const val SNACKBAR_OLD_NAME = "com.google.android.material.snackbar.Snackbar"
const val SNACKBAR_NEW_NAME = "com.tokopedia.unifycomponents.Toaster"

// UnifyButtonDetector
const val BUTTON_ISSUE_ID = "UnifyButtonUsage"
const val BUTTON_OLD_NAME = "Button"
const val BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.UnifyButton"
const val BUTTON_NEW_NAME_SHORT = "com.tokopedia.unifycomponents.UnifyButton"

// UnifyImageButton
const val IMAGE_BUTTON_ISSUE_ID = "UnifyImageButtonUsage"
const val IMAGE_BUTTON_OLD_SHORT_NAME = "ImageButton"
const val IMAGE_BUTTON_NEW_SHORT_NAME = "ImageButtonUnify"
const val IMAGE_BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.UnifyImageButton"





