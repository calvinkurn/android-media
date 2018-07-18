package com.tokopedia.checkout.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.adapter.MultipleAddressItemAdapter;
import com.tokopedia.checkout.view.utils.QuantityTextWatcher;
import com.tokopedia.checkout.view.utils.QuantityWrapper;
import com.tokopedia.design.component.TextViewCompat;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.checkout.view.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressItemViewHolder extends RecyclerView.ViewHolder {

    private static final int SINGLE_DATA_SIZE = 1;
    private static final int QTY_MIN = 1;
    private static final int QTY_MAX = 10000;

    private TextView shippingIndex;
    private TextViewCompat pseudoEditButton;
    private ImageView deleteButton;
    private ViewGroup addressLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private View borderLine;
    private TextView phoneNumber;
    private TextView tvChangeRecipientAddress;
    private TextView tvLabelNoteForSeller;
    private TextView tvBtnShowNotesForSeller;
    private EditText etNotesForSeller;
    private ImageView btnQtyMin;
    private ImageView btnQtyPlus;
    private EditText etQty;
    private TextView tvErrorQtyValidation;
    private View vNotesSeparator;

    private QuantityTextWatcher.QuantityTextwatcherListener quantityTextwatcherListener;
    private MultipleAddressItemData multipleAddressItemData;
    private MultipleAddressItemAdapter multipleAddressItemAdapter;

    public MultipleAddressItemViewHolder(View itemView, CompositeSubscription compositeSubscription,
                                         MultipleAddressItemAdapter multipleAddressItemAdapter) {
        super(itemView);
        this.multipleAddressItemAdapter = multipleAddressItemAdapter;

        shippingIndex = itemView.findViewById(R.id.shipping_index);
        pseudoEditButton = itemView.findViewById(R.id.tv_change_address);
        deleteButton = itemView.findViewById(R.id.delete_button);
        addressLayout = itemView.findViewById(R.id.address_layout);
        addressTitle = itemView.findViewById(R.id.tv_address_name);
        addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);
        address = itemView.findViewById(R.id.tv_recipient_address);
        borderLine = itemView.findViewById(R.id.border_line);
        tvChangeRecipientAddress = itemView.findViewById(R.id.tv_change_recipient_address);
        tvLabelNoteForSeller = itemView.findViewById(R.id.tv_label_note_for_seller);
        tvBtnShowNotesForSeller = itemView.findViewById(R.id.tv_btn_show_notes_for_seller);
        etNotesForSeller = itemView.findViewById(R.id.et_notes_for_seller);
        btnQtyMin = itemView.findViewById(R.id.btn_qty_min);
        btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus);
        etQty = itemView.findViewById(R.id.et_qty);
        tvErrorQtyValidation = itemView.findViewById(R.id.tv_error_qty_validation);
        vNotesSeparator = itemView.findViewById(R.id.v_notes_separator);
        phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);
        phoneNumber.setVisibility(View.GONE);

        initTextWatcherDebouncer(compositeSubscription);
    }

    public void bindItemAdapterAddress(MultipleAddressItemData itemData,
                                       List<MultipleAddressItemData> itemDataList,
                                       MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                                       int position) {
        multipleAddressItemData = itemData;
        renderHeader(itemData, listener, itemDataList, position);
        renderAddress(itemData, listener, itemDataList, position);
        renderNotes(itemData);

        btnQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int qty = Integer.parseInt(multipleAddressItemData.getProductQty());
                    qty = qty + 1;
                    multipleAddressItemData.setProductQty(String.valueOf(qty));
                    multipleAddressItemAdapter.notifyItemChanged(position);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        btnQtyMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int qty = Integer.parseInt(multipleAddressItemData.getProductQty());
                    qty = qty - 1;
                    multipleAddressItemData.setProductQty(String.valueOf(qty));
                    multipleAddressItemAdapter.notifyItemChanged(position);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        String quantity = String.valueOf(itemData.getProductQty());
        etQty.setText(quantity);
        if (quantity.length() > 0) {
            this.etQty.setSelection(quantity.length());
        }

        etQty.addTextChangedListener(new QuantityTextWatcher(quantityTextwatcherListener));

    }

    private void initTextWatcherDebouncer(CompositeSubscription compositeSubscription) {
        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<QuantityWrapper>() {
            @Override
            public void call(final Subscriber<? super QuantityWrapper> subscriber) {
                quantityTextwatcherListener = new QuantityTextWatcher.QuantityTextwatcherListener() {
                    @Override
                    public void onQuantityChanged(QuantityWrapper quantity) {
                        subscriber.onNext(quantity);
                    }
                };
            }
        }).debounce(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QuantityWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(QuantityWrapper quantity) {
                        itemQuantityTextWatcherAction(quantity, multipleAddressItemData);
                    }
                }));
    }

    private void itemQuantityTextWatcherAction(QuantityWrapper quantity, MultipleAddressItemData data) {
        boolean needToUpdateView = !String.valueOf(quantity.getQtyBefore()).equals(quantity.getEditable().toString());
        if (quantity.getEditable().length() != 0) {
            int zeroCount = 0;
            for (int i = 0; i < quantity.getEditable().length(); i++) {
                if (quantity.getEditable().charAt(i) == '0') {
                    zeroCount++;
                } else {
                    break;
                }
            }
            if (zeroCount == quantity.getEditable().length()) {
                data.setProductQty("0");
            } else if (quantity.getEditable().charAt(0) == '0') {
                etQty.setText(quantity.getEditable().toString()
                        .substring(zeroCount, quantity.getEditable().toString().length()));
                etQty.setSelection(etQty.length());
                needToUpdateView = true;
            }
        } else if (TextUtils.isEmpty(etQty.getText())) {
            data.setProductQty("0");
        }

        int qty = 0;
        try {
            qty = Integer.parseInt(quantity.getEditable().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        checkQtyMustDisabled(data, qty);
        data.setProductQty(String.valueOf(qty));
        validateWithAvailableQuantity(data, qty);
        if (needToUpdateView) {
            multipleAddressItemAdapter.notifyItemChanged(getAdapterPosition());
        }
    }

    private void validateWithAvailableQuantity(MultipleAddressItemData data, int qty) {
        if (data.getMaxQuantity() != 0 && qty > data.getMaxQuantity()) {
            String errorMessage = data.getErrorProductMaxQuantity();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String numberAsString = numberFormat.format(data.getMaxQuantity());
            String maxValue = numberAsString.replace(",", ".");
            tvErrorQtyValidation.setText(errorMessage.replace("{{value}}", maxValue));
            tvErrorQtyValidation.setVisibility(View.VISIBLE);
        } else if (qty < data.getMinQuantity()) {
            String errorMessage = data.getErrorProductMinQuantity();
            tvErrorQtyValidation.setText(errorMessage.replace("{{value}}",
                    String.valueOf(data.getMinQuantity())));
            tvErrorQtyValidation.setVisibility(View.VISIBLE);
        } else if (qty > QTY_MAX) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String numberAsString = numberFormat.format(data.getMaxQuantity());
            String maxValue = numberAsString.replace(",", ".");
            String errorMessage = data.getErrorProductMaxQuantity();
            tvErrorQtyValidation.setText(errorMessage.replace("{{value}}", maxValue));
            tvErrorQtyValidation.setVisibility(View.VISIBLE);
        } else {
            tvErrorQtyValidation.setVisibility(View.GONE);
        }
    }

    private void checkQtyMustDisabled(MultipleAddressItemData cartItemHolderData, int qty) {
        if ((qty <= QTY_MIN || qty <= cartItemHolderData.getMinQuantity()) &&
                (qty >= QTY_MAX || (cartItemHolderData.getMaxQuantity() != 0 &&
                        qty >= cartItemHolderData.getMaxQuantity()))) {
            btnQtyMin.setEnabled(false);
            btnQtyPlus.setEnabled(false);
            btnQtyMin.setImageDrawable(ContextCompat.getDrawable(btnQtyMin.getContext(), R.drawable.bg_button_counter_minus_disabled));
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.getContext(), R.drawable.bg_button_counter_plus_disabled));
        } else if (qty <= QTY_MIN || qty <= cartItemHolderData.getMinQuantity()) {
            btnQtyMin.setEnabled(false);
            btnQtyPlus.setEnabled(true);
            btnQtyMin.setImageDrawable(ContextCompat.getDrawable(btnQtyMin.getContext(), R.drawable.bg_button_counter_minus_disabled));
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.getContext(), R.drawable.bg_button_counter_plus));
        } else if (qty >= QTY_MAX || (cartItemHolderData.getMaxQuantity() != 0 &&
                qty >= cartItemHolderData.getMaxQuantity())) {
            btnQtyPlus.setEnabled(false);
            btnQtyMin.setEnabled(true);
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.getContext(), R.drawable.bg_button_counter_plus_disabled));
            btnQtyMin.setImageDrawable(ContextCompat.getDrawable(btnQtyMin.getContext(), R.drawable.bg_button_counter_minus));
        } else {
            btnQtyPlus.setEnabled(true);
            btnQtyMin.setEnabled(true);
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.getContext(), R.drawable.bg_button_counter_plus));
            btnQtyMin.setImageDrawable(ContextCompat.getDrawable(btnQtyMin.getContext(), R.drawable.bg_button_counter_minus));
        }
    }

    private void renderAddress(MultipleAddressItemData itemData,
                               MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                               List<MultipleAddressItemData> itemDataList,
                               int position) {
        addressTitle.setText(itemData.getRecipientAddressModel().getAddressName());
        addressReceiverName.setText(itemData.getRecipientAddressModel().getRecipientName());
        address.setText(itemData.getRecipientAddressModel().getStreet()
                + ", " + itemData.getRecipientAddressModel().getCityName()
                + ", " + itemData.getRecipientAddressModel().getProvinceName()
                + ", " + itemData.getRecipientAddressModel().getRecipientPhoneNumber());
        pseudoEditButton.setVisibility(View.GONE);
        tvChangeRecipientAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeAddress(multipleAddressItemAdapter, position, itemDataList, itemData.getRecipientAddressModel());
            }
        });
    }

    private void renderHeader(MultipleAddressItemData itemData,
                              MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                              List<MultipleAddressItemData> itemDataList,
                              int position) {
        shippingIndex.setText(
                shippingIndex.getText().toString().replace(
                        "#", String.valueOf(itemData.getAddressPosition() + 1)
                )
        );
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItem(multipleAddressItemAdapter, position, itemDataList);
            }
        });
        if (itemDataList.size() == SINGLE_DATA_SIZE) deleteButton.setVisibility(View.GONE);
        else deleteButton.setVisibility(View.VISIBLE);
        if (position == itemDataList.size() - 1) borderLine.setVisibility(View.GONE);
        else borderLine.setVisibility(View.VISIBLE);
    }

    private void renderNotes(MultipleAddressItemData itemData) {
        tvBtnShowNotesForSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotesForSeller.setVisibility(View.VISIBLE);
                tvBtnShowNotesForSeller.setVisibility(View.GONE);
                vNotesSeparator.setVisibility(View.GONE);
                itemData.setStateNotesOpen(true);
            }
        });

        if (StringUtils.isBlank(itemData.getProductNotes())) {
            etNotesForSeller.setVisibility(View.GONE);
            tvBtnShowNotesForSeller.setVisibility(View.VISIBLE);
            etNotesForSeller.setText("");
            vNotesSeparator.setVisibility(View.VISIBLE);
        } else {
            etNotesForSeller.setVisibility(View.VISIBLE);
            tvBtnShowNotesForSeller.setVisibility(View.GONE);
            etNotesForSeller.setText(itemData.getProductNotes());
            etNotesForSeller.setSelection(etNotesForSeller.length());
            vNotesSeparator.setVisibility(View.GONE);
            itemData.setStateNotesOpen(true);
        }

        if (itemData.isStateNotesOpen()) {
            etNotesForSeller.setVisibility(View.VISIBLE);
            tvLabelNoteForSeller.setVisibility(View.GONE);
        }

    }

}
