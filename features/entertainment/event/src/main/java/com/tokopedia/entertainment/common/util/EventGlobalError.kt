package com.tokopedia.entertainment.common.util

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler

object EventGlobalError {

    fun errorEventHandlerGlobalError(context: Context,
                                     throwable: Throwable,
                                     containerError: ViewGroup,
                                     globalError: GlobalError,
                                     defaultAction: () -> Unit = { }
    ) {
        containerError.apply {
            show()
            showUnifyError(throwable, {
                defaultAction()
            }, {
                globalError.show()
                showUnexpectedError(context, globalError, throwable, defaultAction)
            })

            findViewById<GlobalError>(com.tokopedia.globalerror.R.id.globalerror_view)?.let {
                it.gravity = Gravity.CENTER
            }
        }
    }

    private fun showUnexpectedError(context: Context,
                                    globalError: GlobalError,
                                    throwable: Throwable,
                                    defaultAction: () -> Unit = { }
    ) {
        globalError.apply {
            errorTitle.text = ErrorHandler.getErrorMessage(context, throwable)
            setActionClickListener {
                defaultAction()
            }
            errorSecondaryAction.hide()
            errorDescription.hide()
        }
    }
}