package com.tokopedia.contactus.inboxticket2.view.contract;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;


public interface InboxBaseContract {
    interface InboxBaseView extends CustomerView {
        int RESULT_FINISH = 909;

        int REQUEST_DETAILS = 204;

        int REQUEST_IMAGE_PICKER = 145;
        public static final int REQUEST_SUBMIT_FEEDBACK = 0X1;

        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void showProgressBar();

        void hideProgressBar();

        android.view.View getRootView();

        int getRequestCode();

        void showBottomFragment();

        void hideBottomFragment();

        void updateDataSet();

        void setSnackBarErrorMessage(String message, boolean clickable);

        void clearSearch();

        boolean isSearchMode();

        void toggleSearch(int visibility);
        void startActivityForResult(Intent intent, int requestCode);
    }

    interface InboxBasePresenter extends CustomerPresenter<InboxBaseView> {
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onDestroy();

        BottomSheetDialogFragment getBottomFragment(int resID);

        boolean onOptionsItemSelected(MenuItem item);

        void reAttachView();

        void clickCloseSearch();
    }
}
