package com.tokopedia.checkout.view.feature.multipleaddressform.viewholder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressItemAdapter;
import com.tokopedia.checkout.view.common.utils.NoteTextWatcher;
import com.tokopedia.checkout.view.common.utils.QuantityTextWatcher;
import com.tokopedia.checkout.view.common.utils.QuantityWrapper;
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

import static com.tokopedia.checkout.view.common.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressItemViewHolder extends RecyclerView.ViewHolder {

    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";
    private static final int SINGLE_DATA_SIZE = 1;
    private static final int QTY_MIN = 1;
    private static final int QTY_MAX = 10000;
    private static final int TEXTWATCHER_NOTE_DEBOUNCE_TIME = 100;

    private TextView shippingIndex;
    private TextViewCompat pseudoEditButton;
    private ImageView deleteButton;
    private ViewGroup addressLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private TextView addressStatus;
    private View borderLine;
    private TextView phoneNumber;
    private TextView tvChangeRecipientAddress;
    private EditText etNotesForSeller;
    private ImageView btnQtyMin;
    private ImageView btnQtyPlus;
    private EditText etQty;
    private TextView tvErrorQtyValidation;
    private TextView tvErrorNoteValidation;
    private TextView tvNoteCharCounter;

    private QuantityTextWatcher.QuantityTextwatcherListener quantityTextwatcherListener;
    private NoteTextWatcher.NoteTextwatcherListener noteTextwatcherListener;
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
        addressStatus = itemView.findViewById(R.id.tv_address_status);
        borderLine = itemView.findViewById(R.id.border_line);
        tvChangeRecipientAddress = itemView.findViewById(R.id.tv_change_recipient_address);
        etNotesForSeller = itemView.findViewById(R.id.et_notes_for_seller);
        btnQtyMin = itemView.findViewById(R.id.btn_qty_min);
        btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus);
        etQty = itemView.findViewById(R.id.et_qty);
        tvErrorQtyValidation = itemView.findViewById(R.id.tv_error_qty_validation);
        tvErrorNoteValidation = itemView.findViewById(R.id.tv_error_note_validation);
        tvNoteCharCounter = itemView.findViewById(R.id.tv_note_char_counter);
        phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);
        phoneNumber.setVisibility(View.GONE);

        etNotesForSeller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.et_notes_for_seller) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        initTextWatcherDebouncer(compositeSubscription);
    }

    public void bindItemAdapterAddress(MultipleAddressItemData itemData,
                                       List<MultipleAddressItemData> itemDataList,
                                       MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                                       int parentPosition) {
        multipleAddressItemData = itemData;
        renderHeader(itemData, listener, itemDataList, getAdapterPosition());
        renderAddress(itemData, listener, parentPosition);
        renderQuantity(itemData, listener, getAdapterPosition());
        renderNotes(itemData);
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

        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<Editable>() {
            @Override
            public void call(final Subscriber<? super Editable> subscriber) {
                noteTextwatcherListener = new NoteTextWatcher.NoteTextwatcherListener() {
                    @Override
                    public void onNoteChanged(Editable editable) {
                        subscriber.onNext(editable);
                    }
                };
            }
        }).debounce(TEXTWATCHER_NOTE_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Editable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Editable editable) {
                        itemNoteTextWatcherAction(editable, multipleAddressItemData);
                    }
                }));

    }

    private void itemNoteTextWatcherAction(Editable editable, MultipleAddressItemData data) {
        if (!editable.toString().equalsIgnoreCase(data.getProductNotes())) {
            String noteCounter = String.format(tvNoteCharCounter.getContext().getString(R.string.note_counter_format),
                    editable.length(), data.getMaxRemark());
            tvNoteCharCounter.setText(noteCounter);
            data.setProductNotes(editable.toString());
            validateNote(data);
        }
    }

    private void validateNote(MultipleAddressItemData data) {
        if (data.getProductNotes().length() > data.getMaxRemark()) {
            tvErrorNoteValidation.setText(data.getErrorFieldMaxChar()
                    .replace("{{value}}", String.valueOf(data.getMaxRemark())));
            tvErrorNoteValidation.setVisibility(View.VISIBLE);
        } else {
            tvErrorNoteValidation.setVisibility(View.GONE);
        }
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
        data.setProductQty(String.valueOf(qty));
        validateQuantity(data, qty);
        if (needToUpdateView) {
            multipleAddressItemAdapter.notifyItemChanged(getAdapterPosition());
        }
    }

    private void validateQuantity(MultipleAddressItemData data, int qty) {
        checkQtyMustDisabled(data, qty);
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
                               int parentPosition) {
        pseudoEditButton.setVisibility(View.GONE);
        tvChangeRecipientAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeAddress(multipleAddressItemAdapter, itemData.getRecipientAddressModel(),
                        getAdapterPosition(), parentPosition);
            }
        });

        // addressTitle.setVisibility(View.GONE);
        String addressName = itemData.getRecipientAddressModel().getAddressName();
        String recipientName = itemData.getRecipientAddressModel().getRecipientName();
        /*addressName = " (" + addressName + ")";
        recipientName += addressName;
        int startSpan = recipientName.indexOf(addressName);
        int endSpan = recipientName.indexOf(addressName) + addressName.length();
        Spannable formattedPromoMessage = new SpannableString(recipientName);
        final int color = ContextCompat.getColor(addressReceiverName.getContext(), R.color.black_38);
        formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        addressReceiverName.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));*/
        if (itemData.getRecipientAddressModel().getAddressStatus() == 2) {
            addressStatus.setVisibility(View.VISIBLE);
        } else {
            addressStatus.setVisibility(View.GONE);
        }
        addressTitle.setText(addressName);
        addressReceiverName.setText(recipientName);
        String fullAddress = itemData.getRecipientAddressModel().getStreet() + ", "
                + itemData.getRecipientAddressModel().getDestinationDistrictName() + ", "
                + itemData.getRecipientAddressModel().getCityName() + ", "
                + itemData.getRecipientAddressModel().getProvinceName() + ", "
                + itemData.getRecipientAddressModel().getRecipientPhoneNumber();
        address.setText(fullAddress);

    }

    private void renderHeader(MultipleAddressItemData itemData,
                              MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                              List<MultipleAddressItemData> itemDataList,
                              int position) {
        shippingIndex.setText(
                shippingIndex.getText().toString().replace(
                        "#", String.valueOf(getAdapterPosition() + 1)
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
        etNotesForSeller.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(itemData.getMaxRemark())
        });

        if (StringUtils.isBlank(itemData.getProductNotes())) {
            etNotesForSeller.setText("");
        } else {
            etNotesForSeller.setText(itemData.getProductNotes());
            etNotesForSeller.setSelection(etNotesForSeller.length());
        }

        String noteCounter = String.format(tvNoteCharCounter.getContext().getString(R.string.note_counter_format),
                itemData.getProductNotes().length(), itemData.getMaxRemark());
        tvNoteCharCounter.setText(noteCounter);

        if (noteTextwatcherListener != null) {
            etNotesForSeller.addTextChangedListener(new NoteTextWatcher(noteTextwatcherListener));
        } else {
            noteTextwatcherListener = new NoteTextWatcher.NoteTextwatcherListener() {
                @Override
                public void onNoteChanged(Editable editable) {
                    itemNoteTextWatcherAction(editable, multipleAddressItemData);
                }
            };
        }
        validateNote(itemData);
    }

    private void renderQuantity(MultipleAddressItemData itemData,
                                MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
                                int position) {
        btnQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickPlusQuantityButton();
                try {
                    int qty = Integer.parseInt(multipleAddressItemData.getProductQty());
                    qty = qty + 1;
                    if (qty <= itemData.getMaxQuantity()) {
                        multipleAddressItemData.setProductQty(String.valueOf(qty));
                        multipleAddressItemAdapter.notifyItemChanged(position);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        btnQtyMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickMinQuantityButton();
                try {
                    int qty = Integer.parseInt(multipleAddressItemData.getProductQty());
                    qty = qty - 1;
                    if (qty >= itemData.getMinQuantity()) {
                        multipleAddressItemData.setProductQty(String.valueOf(qty));
                        multipleAddressItemAdapter.notifyItemChanged(position);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        etQty.setText(itemData.getProductQty());
        if (itemData.getProductQty().length() > 0) {
            this.etQty.setSelection(itemData.getProductQty().length());
        }

        etQty.addTextChangedListener(new QuantityTextWatcher(quantityTextwatcherListener));
        try {
            int qty = Integer.parseInt(itemData.getProductQty());
            validateQuantity(itemData, qty);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
