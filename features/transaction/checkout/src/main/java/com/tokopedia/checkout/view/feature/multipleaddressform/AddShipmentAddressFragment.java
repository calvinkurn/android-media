package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.AddShipmentAddressComponent;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.DaggerAddShipmentAddressComponent;
import com.tokopedia.checkout.view.di.module.AddShipmentAddressModule;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 20/04/18.
 */
public class AddShipmentAddressFragment extends BaseCheckoutFragment {

    public static final int ADD_MODE = 1;
    public static final int EDIT_MODE = 2;
    private static final int MAX_QTY_DEFAULT = 10000;
    private static final String QTY_MAX_STRING = "10.000";

    private EditText quantityField;
    private EditText notesEditText;
    private ViewGroup addressLayout;
    private ViewGroup notesLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private TextView saveChangesButton;
    private TextView addAddressErrorTextView;
    private ViewGroup chooseAddressButton;
    private ViewGroup quantityErrorLayout;
    private TextView quantityErrorTextView;
    private TextView notesErrorWarningTextView;
    private ImageView decreaseButton;
    private ImageView increaseButton;

    @Inject
    IAddShipmentAddressPresenter presenter;

    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

    @Inject
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;

    private int formMode;
    private int itemPosition;
    private boolean hasSelectRecipientAddress;
    ArrayList<MultipleAddressAdapterData> dataList;
    MultipleAddressAdapterData multipleAddressAdapterData;
    MultipleAddressItemData multipleAddressItemData;

    public static Fragment newInstance(
            int itemPosition,
            ArrayList<MultipleAddressAdapterData> dataList,
            MultipleAddressAdapterData data,
            MultipleAddressItemData addressData,
            int mode
    ) {
        Fragment fragment = new AddShipmentAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS, dataList);
        bundle.putParcelable(AddShipmentAddressActivity.PRODUCT_DATA_EXTRAS, data);
        bundle.putParcelable(AddShipmentAddressActivity.ADDRESS_DATA_EXTRAS, addressData);
        bundle.putInt(AddShipmentAddressActivity.MODE_EXTRA, mode);
        bundle.putInt(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA, itemPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        AddShipmentAddressComponent component = DaggerAddShipmentAddressComponent
                .builder()
                .cartComponent(getComponent(CartComponent.class))
                .addShipmentAddressModule(new AddShipmentAddressModule())
                .build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        formMode = arguments.getInt(AddShipmentAddressActivity.MODE_EXTRA);
        dataList = arguments.getParcelableArrayList(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS);
        multipleAddressAdapterData = arguments.getParcelable(AddShipmentAddressActivity.PRODUCT_DATA_EXTRAS);
        multipleAddressItemData = arguments.getParcelable(AddShipmentAddressActivity.ADDRESS_DATA_EXTRAS);
        itemPosition = arguments.getInt(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.add_shipping_address_fragment;
    }

    @Override
    protected void initView(View view) {
        decreaseButton = view.findViewById(R.id.decrease_quantity);
        increaseButton = view.findViewById(R.id.increase_quantity);
        presenter.initiateData(
                multipleAddressAdapterData,
                multipleAddressItemData);
        TextView senderName = view.findViewById(R.id.sender_name);
        addAddressErrorTextView = view.findViewById(R.id.add_address_error_warning);
        notesErrorWarningTextView = view.findViewById(R.id.note_error_warning);
        quantityErrorLayout = view.findViewById(R.id.quantity_error_layout);
        quantityErrorTextView = view.findViewById(R.id.quantity_error_text_view);
        saveChangesButton = view.findViewById(R.id.save_changes_button);
        setProductView(view, presenter.getMultipleAddressAdapterData(), senderName);
        setProductQuantityView(view, presenter.getMultipleItemData());
        setNotesView(view, presenter.getMultipleItemData());
        setAddressView(view, presenter.getMultipleItemData());
        saveChangesButton.setOnClickListener(onSaveChangesClickedListener());
        if (formMode == ADD_MODE) showChooseAddressButton();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CartAddressChoiceActivity.REQUEST_CODE
                && resultCode == CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS) {
            switch (resultCode) {
                case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                    RecipientAddressModel addressModel =
                            data.getParcelableExtra(
                                    CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA
                            );
                    hasSelectRecipientAddress = true;
                    presenter.setEditableModel(addressModel);
                    showAddressLayout();
                    updateAddressView(presenter.getEditableModel());
                    break;

                default:
                    getActivity().finish();
                    break;
            }
        }
    }

    private void updateAddressView(RecipientAddressModel editableAddress) {
        addressTitle.setText(editableAddress.getAddressName());
        addressReceiverName.setText(editableAddress.getRecipientName());
        address.setText(String.format(
                "%s, %s, %s, %s",
                editableAddress.getStreet(),
                editableAddress.getCityName(),
                editableAddress.getProvinceName(),
                editableAddress.getRecipientPhoneNumber()
        ));
    }

    private void showAddressLayout() {
        addressLayout.setVisibility(View.VISIBLE);
        chooseAddressButton.setVisibility(View.GONE);
        setEditButtonVisibility(quantityField.getText(), presenter.getMultipleItemData());
    }

    private void setEditButtonVisibility(CharSequence charSequence, MultipleAddressItemData data) {
        try {
            if (charSequence.toString().isEmpty() || Integer.parseInt(charSequence.toString()) < 1) {
                quantityField.setText("1");
            } else {
                if (Integer.parseInt(charSequence.toString()) > data.getMaxQuantity()) {
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                    String numberAsString = numberFormat.format(data.getMaxQuantity());
                    String maxValue = numberAsString.replace(",", ".");
                    saveChangesButton.setVisibility(View.GONE);
                    quantityErrorLayout.setVisibility(View.VISIBLE);
                    quantityErrorTextView.setText(data.getErrorProductMaxQuantity()
                            .replace("{{value}}", maxValue));
                } else if (Integer.parseInt(charSequence.toString()) > MAX_QTY_DEFAULT) {
                    saveChangesButton.setVisibility(View.GONE);
                    quantityErrorLayout.setVisibility(View.VISIBLE);
                    quantityErrorTextView.setText(data.getErrorProductMaxQuantity()
                            .replace("{{value}}", QTY_MAX_STRING));
                } else if (Integer.parseInt(charSequence.toString()) < 1) {
                    saveChangesButton.setVisibility(View.GONE);
                    quantityErrorLayout.setVisibility(View.VISIBLE);
                    quantityErrorTextView.setText(data.getErrorProductMinQuantity()
                            .replace("{{value}}", String.valueOf(data.getMinQuantity())));
                } else if (Integer.parseInt(charSequence.toString()) < data.getMinQuantity()) {
                    saveChangesButton.setVisibility(View.GONE);
                    quantityErrorLayout.setVisibility(View.VISIBLE);
                    quantityErrorTextView.setText(data.getErrorProductMinQuantity()
                            .replace("{{value}}", String.valueOf(data.getMinQuantity())));
                } else {
                    quantityErrorLayout.setVisibility(View.GONE);
                }

                if (addressLayout.getVisibility() != View.VISIBLE) {
                    saveChangesButton.setVisibility(View.GONE);
                } else {
                    if (addAddressErrorTextView.getVisibility() != View.VISIBLE &&
                            notesErrorWarningTextView.getVisibility() != View.VISIBLE &&
                            quantityErrorLayout.getVisibility() != View.VISIBLE) {
                        saveChangesButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void openAddressSelectionPage() {
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST);
        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    private void setProductView(View view, MultipleAddressAdapterData productData, TextView senderName) {
        senderName.setText(productData.getSenderName());
        ImageView productImage = view.findViewById(R.id.product_image);
        ImageHandler.LoadImage(productImage, productData.getProductImageUrl());
        TextView productName = view.findViewById(R.id.product_name);
        TextView productPrice = view.findViewById(R.id.product_price);
        productName.setText(productData.getProductName());
        productPrice.setText(productData.getProductPrice());
    }

    private void setProductQuantityView(View view, MultipleAddressItemData itemData) {
        quantityField = view.findViewById(R.id.quantity_field);
        decreaseButton = view.findViewById(R.id.decrease_quantity);
        increaseButton = view.findViewById(R.id.increase_quantity);
        if (formMode == ADD_MODE) {
            if (itemData.getMinQuantity() != 0) {
                quantityField.setText(String.valueOf(itemData.getMinQuantity()));
            } else {
                quantityField.setText(itemData.getMinQuantity() != 0 ? String.valueOf(itemData.getMinQuantity()) : "1");
            }
        } else {
            quantityField.setText(itemData.getProductQty());
        }
        quantityField.setOnClickListener(onQuantityEditTextClicked());
        quantityField.addTextChangedListener(quantityTextWatcher(
                itemData,
                decreaseButton,
                increaseButton
        ));
        decreaseButton.setOnClickListener(onDecreaseButtonClickedListener(quantityField));
        increaseButton.setOnClickListener(onIncreaseButtonClickedListener(quantityField));
        if (formMode == EDIT_MODE) {
            if (itemData.getProductQty().equals("1") || Integer.parseInt(itemData.getProductQty()) <= itemData.getMinQuantity()) {
                decreaseButton.setEnabled(false);
                decreaseButton.setClickable(false);
            } else {
                decreaseButton.setEnabled(true);
                decreaseButton.setClickable(true);
            }

            if (Integer.parseInt(itemData.getProductQty()) >= MAX_QTY_DEFAULT ||
                    Integer.parseInt(itemData.getProductQty()) >= itemData.getMaxQuantity()) {
                increaseButton.setEnabled(false);
                increaseButton.setClickable(false);
            } else {
                increaseButton.setEnabled(true);
                increaseButton.setClickable(true);
            }
        } else if (formMode == ADD_MODE) {
            decreaseButton.setEnabled(false);
            decreaseButton.setClickable(false);
        }
    }

    private void setNotesView(View view, MultipleAddressItemData itemData) {
        ViewGroup emptyNotesLayout = view.findViewById(R.id.empty_notes_layout);
        TextView insertNotesButton = view.findViewById(R.id.insert_notes_button);
        notesLayout = view.findViewById(R.id.notes_layout);
        notesEditText = view.findViewById(R.id.notes_edit_text);
        notesEditText.addTextChangedListener(notesTextWatcher(itemData));
        if (TextUtils.isEmpty(itemData.getProductNotes()) || formMode == ADD_MODE) {
            notesLayout.setVisibility(View.GONE);
            emptyNotesLayout.setVisibility(View.VISIBLE);
            insertNotesButton.setOnClickListener(
                    onInsertNotesButtonClickedListener(emptyNotesLayout, notesLayout)
            );
        } else {
            emptyNotesLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.VISIBLE);
            if (formMode == EDIT_MODE) {
                notesEditText.setText(itemData.getProductNotes());
            }
        }
    }

    private void showChooseAddressButton() {
        addressLayout.setVisibility(View.GONE);
        chooseAddressButton.setVisibility(View.VISIBLE);
        saveChangesButton.setVisibility(View.GONE);
    }

    private void setAddressView(View view, MultipleAddressItemData itemData) {
        addressLayout = view.findViewById(R.id.address_layout);
        addressTitle = view.findViewById(R.id.address_title);
        addressReceiverName = view.findViewById(R.id.address_receiver_name);
        address = view.findViewById(R.id.address);
        addressLayout.setOnClickListener(onAddressLayoutClickedListener());
        chooseAddressButton = view.findViewById(R.id.choose_address_button);
        chooseAddressButton.setOnClickListener(onChooseAddressClickedListener());
    }

    private void addNewAddressItem() {
        Intent intent = new Intent();
        MultipleAddressItemData newItemData = presenter.confirmAddData(
                multipleAddressItemData,
                quantityField.getText().toString(),
                checkNotesAvailability(notesLayout.getVisibility() == View.VISIBLE, notesEditText)
        );
        intent.putExtra(AddShipmentAddressActivity.ADDRESS_DATA_RESULT, newItemData);
        intent.putExtra(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS, dataList);
        intent.putExtra(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA, itemPosition);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void changeAddressData() {
        Intent intent = new Intent();
        MultipleAddressItemData editedItemData = presenter.confirmEditData(
                quantityField.getText().toString(),
                checkNotesAvailability(notesLayout.getVisibility() == View.VISIBLE, notesEditText)
        );
        intent.putExtra(AddShipmentAddressActivity.ADDRESS_DATA_RESULT, editedItemData);
        intent.putExtra(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS, dataList);
        intent.putExtra(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA, itemPosition);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private String checkNotesAvailability(boolean notesFieldShown, EditText notesEditText) {
        if (notesFieldShown)
            return notesEditText.getText().toString();
        else return "";
    }

    private void setQuantityButtonAvailability(CharSequence charSequence,
                                               ImageView decreaseButton,
                                               ImageView increaseButton) {
        try {
            int quantity = Integer.parseInt(charSequence.toString());
            if (charSequence.toString().isEmpty() || quantity == 0) {
                decreaseButton.setClickable(false);
                decreaseButton.setEnabled(false);
                increaseButton.setClickable(false);
                increaseButton.setEnabled(false);
            } else if (quantity == 1 || quantity <= multipleAddressItemData.getMinQuantity()) {
                decreaseButton.setClickable(false);
                decreaseButton.setEnabled(false);
                increaseButton.setClickable(true);
                increaseButton.setEnabled(true);
            } else if (quantity == MAX_QTY_DEFAULT || quantity >= multipleAddressItemData.getMaxQuantity()) {
                decreaseButton.setClickable(true);
                decreaseButton.setEnabled(true);
                increaseButton.setClickable(false);
                increaseButton.setEnabled(false);
            } else {
                decreaseButton.setClickable(true);
                decreaseButton.setEnabled(true);
                increaseButton.setClickable(true);
                increaseButton.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setNotesSubmitButtonVisibility(
            String notes,
            MultipleAddressItemData data
    ) {
        if (notes.length() > data.getMaxRemark()) {
            notesErrorWarningTextView.setVisibility(View.VISIBLE);
            if (data.getErrorFieldMaxChar() != null) {
                notesErrorWarningTextView.setText(data.getErrorFieldMaxChar()
                        .replace("{{value}}", String.valueOf(data.getMaxRemark())));
                saveChangesButton.setVisibility(View.GONE);
            }
        } else {
            notesErrorWarningTextView.setVisibility(View.GONE);
            if (quantityErrorLayout.getVisibility() != View.VISIBLE &&
                    addAddressErrorTextView.getVisibility() != View.VISIBLE) {
                if (formMode == ADD_MODE) {
                    if (hasSelectRecipientAddress) {
                        saveChangesButton.setVisibility(View.VISIBLE);
                    } else {
                        saveChangesButton.setVisibility(View.GONE);
                    }
                } else {
                    saveChangesButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private View.OnClickListener onSaveChangesClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressLayout.getVisibility() == View.VISIBLE) {
                    addAddressErrorTextView.setVisibility(View.GONE);
                    checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickSimpanFromUbahFromKirimKeBeberapaAlamat();
                    if (formMode == ADD_MODE) {
                        addNewAddressItem();
                    } else {
                        changeAddressData();
                    }
                } else {
                    addAddressErrorTextView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private View.OnClickListener onQuantityEditTextClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) view).selectAll();
                String qtyString = ((EditText) view).getText().toString();
                checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickInputQuantityFromUbahFromKirimKeBeberapaAlamat(
                        !TextUtils.isEmpty(qtyString) ? qtyString : ""
                );
            }
        };
    }

    private TextWatcher quantityTextWatcher(final MultipleAddressItemData data,
                                            final ImageView decreaseButton,
                                            final ImageView increaseButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    quantityField.setText(data.getMinQuantity() != 0 ? String.valueOf(data.getMinQuantity()) : "1");
                } else {
                    int zeroCount = 0;
                    for (int i = 0; i < editable.length(); i++) {
                        if (editable.charAt(i) == '0') {
                            zeroCount++;
                        }
                    }
                    if (zeroCount == editable.length()) {
                        quantityField.setText(data.getMinQuantity() != 0 ? String.valueOf(data.getMinQuantity()) : "1");
                    } else if (editable.charAt(0) == '0') {
                        quantityField.setText(editable.toString().substring(zeroCount, editable.length()));
                    }
                    setQuantityButtonAvailability(editable, decreaseButton, increaseButton);
                    setEditButtonVisibility(editable, data);
                    quantityField.setSelection(quantityField.length());
                }
            }
        };
    }

    private View.OnClickListener onDecreaseButtonClickedListener(final EditText quantityField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText().toString());
                    quantityField.setText(String.valueOf(quantity - 1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener onIncreaseButtonClickedListener(final EditText quantityField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText().toString());
                    quantityField.setText(String.valueOf(quantity + 1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private TextWatcher notesTextWatcher(final MultipleAddressItemData data) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setNotesSubmitButtonVisibility(editable.toString(), data);
            }
        };
    }

    private View.OnClickListener onAddressLayoutClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressSelectionPage();
            }
        };
    }

    private View.OnClickListener onChooseAddressClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressSelectionPage();
            }
        };
    }

    private View.OnClickListener onInsertNotesButtonClickedListener(final ViewGroup emptyNotesLayout,
                                                                    final ViewGroup notesLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyNotesLayout.setVisibility(View.GONE);
                notesLayout.setVisibility(View.VISIBLE);
                checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickTulisCatatanFromUbahFromKirimKeBeberapaAlamat();
            }
        };
    }

    public void onCloseButtonPressed() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickXFromUbahFromKirimKeBeberapaAlamat();
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.EDIT_MULTIPLE_ADDRESS_PAGE;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkoutAnalyticsChangeAddress.sendScreenName(getActivity(), getScreenName());
    }
}
