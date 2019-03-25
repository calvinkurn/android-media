package view.viewcontrollers;

import android.app.Activity;
import android.content.Intent;

public interface TradeInBaseView {
        int RESULT_FINISH = 909;

        int REQUEST_DETAILS = 204;

        int REQUEST_IMAGE_PICKER = 145;

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
}
