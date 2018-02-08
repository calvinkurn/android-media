package com.tokopedia.digital.widget.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;
import com.tokopedia.digital.widget.model.WidgetContact;
import com.tokopedia.digital.widget.model.category.Category;

import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nabillasabbaha on 7/18/17.
 * Modified by rizkyfadillah on 10/17/2017.
 */

@RuntimePermissions
public abstract class BaseWidgetRechargeFragment<P> extends BasePresenterFragmentV4<P> {

    protected static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    protected static final String ARG_TAB_INDEX_POSITION = "ARG_TAB_INDEX_POSITION";

    private static final String EXTRA_CHECKOUT_PASS_DATA = "EXTRA_CHECKOUT_PASS_DATA";
    private static final String STATE_CATEGORY = "STATE_CATEGORY";

    protected static final int CONTACT_PICKER_RESULT = 1001;
    protected static final int LOGIN_REQUEST_CODE = 198;

    private static final String PHONE_CODE = "62";
    private static final String PHONE_CODE_PLUS = "+62";
    private static final String DEFAULT_PREFIX_PHONE = "0";

    protected Category category;
    protected int currentPosition;
    protected boolean useCache;

    protected Unbinder unbinder;
    protected Bundle bundle;

    protected DigitalCheckoutPassData digitalCheckoutPassDataState;

    protected String lastOperatorSelected = "";
    protected String lastProductSelected = "";

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser)
            hideKeyboard();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(STATE_CATEGORY, category);
        state.putParcelable(EXTRA_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (savedState != null) {
            category = savedState.getParcelable(STATE_CATEGORY);
            digitalCheckoutPassDataState = savedState.getParcelable(
                    EXTRA_CHECKOUT_PASS_DATA
            );
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        category = arguments.getParcelable(ARG_PARAM_CATEGORY);
        currentPosition = arguments.getInt(ARG_TAB_INDEX_POSITION);
        bundle = arguments;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        WidgetContact contact;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    try {
                        Uri contactURI = intent.getData();
                        contact = fetchAndBuildContact(getActivity(), contactURI);
                        getPhoneNumberAndDisplayIt(contact);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                    if (intent != null && intent.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = intent.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            NetworkErrorHelper.showSnackbar(getActivity(), message);
                        }
                    }
                    break;
                case LOGIN_REQUEST_CODE:
                    if (SessionHandler.isV4Login(getActivity()) && digitalCheckoutPassDataState != null) {
                        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
                            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
                            startActivityForResult(
                                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                            );
                        }
                    }
                    break;
            }
        }
    }

    public abstract void saveAndDisplayPhoneNumber(String phoneNumber);

    protected void clearHolder(LinearLayout holderView) {
        if (holderView.getChildCount() > 0) {
            holderView.removeAllViews();
        }
    }

    private void getPhoneNumberAndDisplayIt(WidgetContact contact) {
        String phoneNumber = contact.contactNumber;
        phoneNumber = validateTextPrefix(phoneNumber);
        saveAndDisplayPhoneNumber(phoneNumber);
    }

    protected String validateTextPrefix(String phoneNumber) {
        if (phoneNumber.startsWith(PHONE_CODE)) {
            phoneNumber = phoneNumber.replaceFirst(PHONE_CODE, DEFAULT_PREFIX_PHONE);
        }
        if (phoneNumber.startsWith(PHONE_CODE_PLUS)) {
            phoneNumber = phoneNumber.replace(PHONE_CODE_PLUS, DEFAULT_PREFIX_PHONE);
        }
        phoneNumber = phoneNumber.replace(".", "");

        return phoneNumber.replaceAll("[^0-9]+", "");
    }

    private WidgetContact fetchAndBuildContact(Context ctx, Uri uriContact) {
        String id = uriContact.getLastPathSegment();
        WidgetContact contact = new WidgetContact();
        contact = buildContactPhoneDetails(ctx, contact, id);
        return contact;
    }

    private WidgetContact buildContactPhoneDetails(Context ctx, final WidgetContact contact, String id) {
        ContentResolver contentResolver = ctx.getContentResolver();
        String contactWhere = ContactsContract.CommonDataKinds.Phone._ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?";

        String[] contactWhereParams = new String[]{
                id,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        };
        Cursor cursorPhone = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                contactWhere,
                contactWhereParams,
                null
        );

        mappingCursorToObjectContact(contact, cursorPhone);
        return contact;
    }

    private void mappingCursorToObjectContact(WidgetContact widgetContact, Cursor cursorPhone) {
        if (cursorPhone != null) {
            if (cursorPhone.getCount() > 0) {
                if (cursorPhone.moveToNext()) {
                    if (Integer.parseInt(cursorPhone.getString(
                            cursorPhone.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0
                            ) {
                        String givenName = cursorPhone.getString(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.Contacts.DISPLAY_NAME
                                )
                        );

                        int contactType = cursorPhone.getInt(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE
                                )
                        );
                        widgetContact.contactNumber = cursorPhone.getString(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                        );
                        widgetContact.givenName = givenName;
                        widgetContact.contactType = contactType;
                    }
                    cursorPhone.moveToNext();
                }
            }
            cursorPhone.close();
        }
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void doLaunchContactPicker() {
        Intent contactPickerIntent = new Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        );
        try {
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            NetworkErrorHelper.showSnackbar(getActivity(),
                    getString(R.string.error_message_contact_not_found));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseWidgetRechargeFragmentPermissionsDispatcher.onRequestPermissionsResult(
                this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForContacts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_CONTACTS);

    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForContacts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_CONTACTS);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForContacts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_CONTACTS);
    }

    protected void setRechargeEditTextTouchCallback(WidgetClientNumberView widgetClientNumberView) {
        if (widgetClientNumberView != null) {
            widgetClientNumberView.getAutocompleteView().setOnTouchListener(getOnTouchListener(widgetClientNumberView));
        }
    }

    protected void removeRechargeEditTextCallback(WidgetClientNumberView widgetClientNumberView) {
        if (widgetClientNumberView != null) {
            widgetClientNumberView.getAutocompleteView().setOnFocusChangeListener(null);
        }
    }

    private void hideKeyboard() {
        if (getView() != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(
                    getView().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }

    private View.OnTouchListener getOnTouchListener(final WidgetClientNumberView widgetClientNumberView) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        widgetClientNumberView.getAutocompleteView().setFocusable(true);
                        widgetClientNumberView.getAutocompleteView().requestFocus();
                        break;
                    case MotionEvent.ACTION_UP:
//                        setParentToScroolToTop();
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
    }

    protected void showSnackbarErrorMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}