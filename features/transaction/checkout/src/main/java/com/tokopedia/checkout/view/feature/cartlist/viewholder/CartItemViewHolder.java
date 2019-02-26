package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.utils.NoteTextWatcher;
import com.tokopedia.checkout.view.common.utils.QuantityTextWatcher;
import com.tokopedia.checkout.view.common.utils.QuantityWrapper;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.design.utils.CurrencyFormatUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.checkout.view.common.utils.NoteTextWatcher.TEXTWATCHER_NOTE_DEBOUNCE_TIME;
import static com.tokopedia.checkout.view.common.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartItemViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new;
    private static final int QTY_MIN = 1;
    private static final int QTY_MAX = 10000;
    private static final int MAX_SHOWING_NOTES_CHAR = 20;

    private final Context context;
    private final CartItemAdapter.ActionListener actionListener;
    private ViewHolderListener viewHolderListener;

    private LinearLayout llWarningAndError;
    private FrameLayout flCartItemContainer;
    private CheckBox cbSelectItem;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private AppCompatEditText etQty;
    private ImageView btnQtyPlus;
    private ImageView btnQtyMinus;
    private ImageView ivIconFreeReturn;
    private TextView tvInfoPreOrder;
    private TextView tvInfoCashBack;
    private TextView tvCodBadge;
    private AppCompatEditText etRemark;
    private TextView tvLabelRemarkOption;
    private ImageView btnDelete;
    private TextView tvErrorFormValidation;
    private TextView tvErrorFormRemarkValidation;
    private LinearLayout layoutError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private LinearLayout layoutWarning;
    private TextView tvWarningTitle;
    private TextView tvWarningDescription;
    private TextView tvNoteCharCounter;
    private FlexboxLayout productProperties;
    private TextView tvRemark;
    private TextView tvLabelFormRemark;
    private ImageView imgWishlist;
    private TextView tvEllipsize;
    private View divider;

    private CartItemHolderData cartItemHolderData;
    private QuantityTextWatcher.QuantityTextwatcherListener quantityTextwatcherListener;
    private NoteTextWatcher.NoteTextwatcherListener noteTextwatcherListener;
    private int parentPosition;
    private int dataSize;

    @SuppressLint("ClickableViewAccessibility")
    public CartItemViewHolder(View itemView, CompositeSubscription cadapterCmpositeSubscription,
                              CartItemAdapter.ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.context = itemView.getContext();

        this.llWarningAndError = itemView.findViewById(R.id.ll_warning_and_error);
        this.flCartItemContainer = itemView.findViewById(R.id.fl_cart_item_container);
        this.cbSelectItem = itemView.findViewById(R.id.cb_select_item);
        this.tvErrorFormValidation = itemView.findViewById(R.id.tv_error_form_validation);
        this.tvErrorFormRemarkValidation = itemView.findViewById(R.id.tv_error_form_remark_validation);
        this.ivProductImage = itemView.findViewById(R.id.iv_image_product);
        this.tvProductName = itemView.findViewById(R.id.tv_product_name);
        this.tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        this.etQty = itemView.findViewById(R.id.et_qty);
        this.btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus);
        this.btnQtyMinus = itemView.findViewById(R.id.btn_qty_min);
        this.ivIconFreeReturn = itemView.findViewById(R.id.iv_free_return_icon);
        this.tvInfoPreOrder = itemView.findViewById(R.id.tv_pre_order);
        this.tvInfoCashBack = itemView.findViewById(R.id.tv_cashback);
        this.tvCodBadge = itemView.findViewById(R.id.tv_cod);
        this.tvLabelRemarkOption = itemView.findViewById(R.id.tv_label_remark_option);
        this.etRemark = itemView.findViewById(R.id.et_remark);
        this.btnDelete = itemView.findViewById(R.id.btn_delete_cart);
        this.layoutError = itemView.findViewById(R.id.layout_error);
        this.tvErrorTitle = itemView.findViewById(R.id.tv_error_title);
        this.tvErrorDescription = itemView.findViewById(R.id.tv_error_description);
        this.layoutWarning = itemView.findViewById(R.id.layout_warning);
        this.tvWarningTitle = itemView.findViewById(R.id.tv_warning_title);
        this.tvWarningDescription = itemView.findViewById(R.id.tv_warning_description);
        this.tvNoteCharCounter = itemView.findViewById(R.id.tv_note_char_counter);
        this.productProperties = itemView.findViewById(R.id.product_properties);
        this.tvRemark = itemView.findViewById(R.id.tv_remark);
        this.tvLabelFormRemark = itemView.findViewById(R.id.tv_label_form_remark);
        this.imgWishlist = itemView.findViewById(R.id.img_wishlist);
        this.tvEllipsize = itemView.findViewById(R.id.tv_ellipsize);
        this.divider = itemView.findViewById(R.id.holder_item_cart_divider);

        etRemark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.et_remark) {
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

        initTextwatcherDebouncer(cadapterCmpositeSubscription);
    }

    private void initTextwatcherDebouncer(CompositeSubscription compositeSubscription) {
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
                .subscribeOn(Schedulers.io())
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
                        itemQuantityTextWatcherAction(quantity);
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
                .subscribeOn(Schedulers.io())
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
                        itemNoteTextWatcherAction(editable);
                    }
                }));
    }

    public void bindData(final CartItemHolderData data, int parentPosition, ViewHolderListener viewHolderListener, int dataSize) {
        this.viewHolderListener = viewHolderListener;
        this.parentPosition = parentPosition;
        cartItemHolderData = data;
        this.dataSize = dataSize;

        renderProductInfo(data, parentPosition);
        renderRemark(data, parentPosition, viewHolderListener);
        renderQuantity(data, parentPosition, viewHolderListener);
        renderErrorFormItemValidation(data);
        renderWarningAndError(data);
        renderWishlist(data);
        renderSelection(data, parentPosition);
    }

    private void renderSelection(CartItemHolderData data, int parentPosition) {
        cbSelectItem.setEnabled(!data.getCartItemData().isError());
        cbSelectItem.setChecked(!data.getCartItemData().isError() && data.isSelected());
        cbSelectItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!data.getCartItemData().isError()) {
                    data.setSelected(isChecked);
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        actionListener.onCartItemCheckChanged(getAdapterPosition(), parentPosition, data.isSelected());
                        viewHolderListener.onNeedToRefreshAllShop();
                    }
                }
            }
        });
    }

    private void renderWarningAndError(CartItemHolderData data) {
        if (data.getCartItemData().isParentHasErrorOrWarning()) {
            if (data.getCartItemData().isSingleChild()) {
                renderErrorItemHeader(data);
                renderWarningItemHeader(data);
                setWarningAndErrorVisibility(data);
            } else {
                disableView(data);
            }
        } else {
            if (!data.getCartItemData().isSingleChild()) {
                renderErrorItemHeader(data);
                renderWarningItemHeader(data);
                setWarningAndErrorVisibility(data);
            } else {
                disableView(data);
            }
        }
    }

    private void setWarningAndErrorVisibility(CartItemHolderData data) {
        if ((!TextUtils.isEmpty(data.getCartItemData().getErrorMessageTitle()) ||
                !TextUtils.isEmpty(data.getCartItemData().getWarningMessageTitle())) &&
                (data.getCartItemData().isError() || data.getCartItemData().isWarning())) {
            llWarningAndError.setVisibility(View.VISIBLE);
        } else {
            llWarningAndError.setVisibility(View.GONE);
        }
    }

    private void disableView(CartItemHolderData data) {
        if (data.getCartItemData().isError()) {
            flCartItemContainer.setForeground(ContextCompat.getDrawable(flCartItemContainer.getContext(), R.drawable.fg_disabled_item));
            btnDelete.setImageResource(R.drawable.ic_delete_cart_bold);
        } else {
            flCartItemContainer.setForeground(ContextCompat.getDrawable(flCartItemContainer.getContext(), R.drawable.fg_enabled_item));
            btnDelete.setImageResource(R.drawable.ic_delete_cart);
        }
        llWarningAndError.setVisibility(View.GONE);
    }

    private void renderProductInfo(CartItemHolderData data, int parentPosition) {
        if (cartItemHolderData.getCartItemData().getOriginData().getInvenageValue() == 0) {
            cartItemHolderData.getCartItemData().getOriginData().setInvenageValue(QTY_MAX);
        }
        this.tvProductName.setText(
                Html.fromHtml(data.getCartItemData().getOriginData().getProductName())
        );
        if (data.getCartItemData().getOriginData().getWholesalePriceFormatted() != null) {
            this.tvProductPrice.setText(data.getCartItemData().getOriginData().getWholesalePriceFormatted());
        } else {
            this.tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    data.getCartItemData().getOriginData().getPricePlan(), false));
        }

        ImageHandler.loadImageRounded2(
                this.itemView.getContext(), this.ivProductImage,
                data.getCartItemData().getOriginData().getProductImage()
        );

        this.ivProductImage.setOnClickListener(getOnClickProductItemListener(getAdapterPosition(), parentPosition, data));
        this.tvProductName.setOnClickListener(getOnClickProductItemListener(getAdapterPosition(), parentPosition, data));

        if (data.getCartItemData().getOriginData().isFreeReturn()) {
            this.ivIconFreeReturn.setVisibility(View.VISIBLE);
            ImageHandler.loadImageRounded2(
                    this.itemView.getContext(), this.ivIconFreeReturn,
                    data.getCartItemData().getOriginData().getFreeReturnLogo()
            );
        } else {
            this.ivIconFreeReturn.setVisibility(View.GONE);
        }

        if (data.getCartItemData().getOriginData().isPreOrder()) {
            this.tvInfoPreOrder.setText(data.getCartItemData().getOriginData().getPreOrderInfo());
            this.tvInfoPreOrder.setVisibility(View.VISIBLE);
        } else {
            this.tvInfoPreOrder.setVisibility(View.GONE);
        }

        this.tvCodBadge.setVisibility(
                data.getCartItemData().getOriginData().isCod() ? View.VISIBLE : View.GONE
        );

        this.tvInfoCashBack.setVisibility(
                data.getCartItemData().getOriginData().isCashBack() ? View.VISIBLE : View.GONE
        );

        this.tvInfoCashBack.setText(data.getCartItemData().getOriginData().getCashBackInfo());

        if (data.getCartItemData().getOriginData().isCashBack() ||
                data.getCartItemData().getOriginData().isPreOrder() ||
                data.getCartItemData().getOriginData().isFreeReturn() ||
                data.getCartItemData().getOriginData().isCod()) {
            productProperties.setVisibility(View.VISIBLE);
        } else {
            productProperties.setVisibility(View.GONE);
        }

        btnDelete.setOnClickListener(view -> {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                actionListener.onCartItemDeleteButtonClicked(data, getAdapterPosition(), parentPosition);
            }
        });

        divider.setVisibility((getLayoutPosition() == dataSize - 1) ? View.GONE : View.VISIBLE);
    }

    private void renderRemark(CartItemHolderData data, int parentPosition, ViewHolderListener viewHolderListener) {
        this.etRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    actionListener.onCartItemRemarkEditChange(
                            data.getCartItemData(), textView.getText().toString(), getAdapterPosition(), parentPosition
                    );
                    return true;
                }
                return false;
            }
        });

        this.tvLabelRemarkOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getCartItemData().isError()) {
                    actionListener.onCartItemLabelInputRemarkClicked();
                    if (tvLabelRemarkOption.getText().equals(tvLabelRemarkOption.getContext().getString(
                            R.string.label_button_change_note))) {
                        String remark = data.getCartItemData().getUpdatedData().getRemark();
                        remark = remark + " ";
                        data.getCartItemData().getUpdatedData().setRemark(remark);
                    }
                    data.setStateRemarkExpanded(true);
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        viewHolderListener.onNeedToRefreshSingleProduct(getAdapterPosition());
                    }
                }
            }
        });

        if (!StringUtils.isBlank(data.getCartItemData().getUpdatedData().getRemark())) {
            data.setStateRemarkExpanded(true);
        }

        if (data.isStateRemarkExpanded()) {
            // Has a notes from pdp or not at all but click add notes button
            if (StringUtils.isBlank(data.getCartItemData().getOriginData().getOriginalRemark()) ||
                    !data.getCartItemData().getUpdatedData().getRemark().equals(
                            data.getCartItemData().getOriginData().getOriginalRemark())) {
                // Notes is empty after click add notes button or has value after use click change notes button
                this.tvLabelFormRemark.setVisibility(View.VISIBLE);
                this.tvRemark.setVisibility(View.GONE);
                this.etRemark.setText(data.getCartItemData().getUpdatedData().getRemark());
                this.etRemark.setVisibility(View.VISIBLE);
                this.etRemark.setSelection(etRemark.length());
                this.tvLabelRemarkOption.setVisibility(View.GONE);
                this.tvNoteCharCounter.setVisibility(View.VISIBLE);
                this.tvEllipsize.setVisibility(View.GONE);
            } else {
                // Has notes from pdp
                this.tvLabelFormRemark.setVisibility(View.GONE);
                this.etRemark.setVisibility(View.GONE);
                this.tvRemark.setText(data.getCartItemData().getUpdatedData().getRemark());
                this.tvRemark.setVisibility(View.VISIBLE);
                this.tvLabelRemarkOption.setVisibility(View.VISIBLE);
                this.tvNoteCharCounter.setVisibility(View.GONE);
                this.tvLabelRemarkOption.setText(tvLabelRemarkOption.getContext().getString(R.string.label_button_change_note));
                if (data.getCartItemData().getUpdatedData().getRemark().length() >= MAX_SHOWING_NOTES_CHAR) {
                    this.tvEllipsize.setVisibility(View.VISIBLE);
                } else {
                    this.tvEllipsize.setVisibility(View.GONE);
                }
            }
        } else {
            // No notes at all
            this.etRemark.setVisibility(View.GONE);
            this.tvRemark.setVisibility(View.GONE);
            this.tvNoteCharCounter.setVisibility(View.GONE);
            this.tvLabelFormRemark.setVisibility(View.GONE);
            this.tvLabelRemarkOption.setText(tvLabelRemarkOption.getContext().getString(R.string.label_button_add_note));
            this.tvLabelRemarkOption.setVisibility(View.VISIBLE);
            this.etRemark.setText("");
            this.tvEllipsize.setVisibility(View.GONE);
        }

        this.etRemark.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(data.getCartItemData().getUpdatedData().getMaxCharRemark())
        });
        this.etRemark.addTextChangedListener(new NoteTextWatcher(noteTextwatcherListener));
    }

    private void renderQuantity(CartItemHolderData data, int parentPosition, ViewHolderListener viewHolderListener) {
        String quantity = String.valueOf(data.getCartItemData().getUpdatedData().getQuantity());
        this.etQty.setText(String.valueOf(data.getCartItemData().getUpdatedData().getQuantity()));
        if (quantity.length() > 0) {
            this.etQty.setSelection(quantity.length());
        }
        this.etQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.getCartItemData().isError()) {
                    String qtyStr = ((AppCompatEditText) v).getText().toString();
                    actionListener.onCartItemQuantityInputFormClicked(
                            !TextUtils.isEmpty(qtyStr) ? qtyStr : ""
                    );
                }
            }
        });

        this.btnQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getCartItemData().isError()) {
                    try {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            actionListener.onCartItemQuantityPlusButtonClicked(data, getAdapterPosition(), parentPosition);
                            validateWithAvailableQuantity(cartItemHolderData, Integer.parseInt(etQty.getText().toString()));
                            handleRefreshType(data, viewHolderListener, parentPosition);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.btnQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getCartItemData().isError()) {
                    try {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            actionListener.onCartItemQuantityMinusButtonClicked(data, getAdapterPosition(), parentPosition);
                            validateWithAvailableQuantity(cartItemHolderData, Integer.parseInt(etQty.getText().toString()));
                            handleRefreshType(data, viewHolderListener, parentPosition);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!TextUtils.isEmpty(etQty.getText().toString())) {
            checkQtyMustDisabled(cartItemHolderData, Integer.parseInt(etQty.getText().toString()));
        }
        this.etQty.addTextChangedListener(new QuantityTextWatcher(quantityTextwatcherListener));
        this.etQty.setEnabled(!data.getCartItemData().isError());
    }

    private void handleRefreshType(CartItemHolderData data, ViewHolderListener viewHolderListener, int parentPosition) {
        if (data.getCartItemData().getOriginData().getWholesalePrice() != null &&
                data.getCartItemData().getOriginData().getWholesalePrice().size() > 0) {
            if (data.getCartItemData().getOriginData().isPreOrder()) {
                viewHolderListener.onNeedToRefreshAllShop();
            } else {
                viewHolderListener.onNeedToRefreshSingleShop(parentPosition);
            }
        } else {
            viewHolderListener.onNeedToRefreshSingleProduct(getAdapterPosition());
        }
    }

    private void renderWishlist(CartItemHolderData data) {
        if (data.getCartItemData().getOriginData().isWishlisted()) {
            imgWishlist.setImageResource(R.drawable.ic_wishlist_checkout_on);
        } else {
            imgWishlist.setImageResource(R.drawable.ic_wishlist_checkout_off);
        }

        imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !data.getCartItemData().getOriginData().isWishlisted();
                actionListener.onWishlistCheckChanged(data.getCartItemData().getOriginData().getProductId(), checked);
            }
        });
    }

    @NonNull
    private View.OnClickListener getOnClickProductItemListener(
            @SuppressLint("RecyclerView") final int position, final int parentPosition,
            final CartItemHolderData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    actionListener.onCartItemProductClicked(data, position, parentPosition);
                }
            }
        };
    }

    private void renderErrorFormItemValidation(CartItemHolderData data) {
        String noteCounter = String.format(tvNoteCharCounter.getContext().getString(R.string.note_counter_format),
                data.getCartItemData().getUpdatedData().getRemark().length(),
                data.getCartItemData().getUpdatedData().getMaxCharRemark());
        tvNoteCharCounter.setText(noteCounter);
        if (data.getErrorFormItemValidationType() == CartItemHolderData.ERROR_EMPTY) {
            this.tvErrorFormValidation.setText("");
            this.tvErrorFormValidation.setVisibility(View.GONE);
            this.tvErrorFormRemarkValidation.setVisibility(View.GONE);
            this.tvErrorFormRemarkValidation.setText("");
        } else {
            if (data.getErrorFormItemValidationType() == CartItemHolderData.ERROR_FIELD_MAX_CHAR) {
                if (TextUtils.isEmpty(data.getErrorFormItemValidationMessage())) {
                    this.tvErrorFormValidation.setText("");
                    this.tvErrorFormValidation.setVisibility(View.GONE);
                }
                this.tvErrorFormRemarkValidation.setVisibility(View.VISIBLE);
                this.tvErrorFormRemarkValidation.setText(data.getErrorFormItemValidationMessage());
                this.tvNoteCharCounter.setVisibility(View.GONE);
            } else {
                this.tvErrorFormValidation.setText(data.getErrorFormItemValidationMessage());
                this.tvErrorFormValidation.setVisibility(View.VISIBLE);
                this.tvErrorFormRemarkValidation.setVisibility(View.GONE);
                this.tvErrorFormRemarkValidation.setText("");
                if (!data.getCartItemData().getOriginData().getOriginalRemark().equals(
                        data.getCartItemData().getUpdatedData().getRemark())) {
                    this.tvNoteCharCounter.setVisibility(View.VISIBLE);
                } else {
                    this.tvNoteCharCounter.setVisibility(View.GONE);
                }
            }
        }
        actionListener.onCartItemAfterErrorChecked();
    }

    private void renderErrorItemHeader(CartItemHolderData data) {
        if (data.getCartItemData().isError()) {
            flCartItemContainer.setForeground(ContextCompat.getDrawable(flCartItemContainer.getContext(), R.drawable.fg_disabled_item));
            btnDelete.setImageResource(R.drawable.ic_delete_cart_bold);
            tvErrorTitle.setText(data.getCartItemData().getErrorMessageTitle());
            String errorDescription = data.getCartItemData().getErrorMessageDescription();
            if (!TextUtils.isEmpty(errorDescription)) {
                tvErrorDescription.setText(errorDescription);
                tvErrorDescription.setVisibility(View.VISIBLE);
            } else {
                tvErrorDescription.setVisibility(View.GONE);
            }
            layoutError.setVisibility(View.VISIBLE);
        } else {
            flCartItemContainer.setForeground(ContextCompat.getDrawable(flCartItemContainer.getContext(), R.drawable.fg_enabled_item));
            btnDelete.setImageResource(R.drawable.ic_delete_cart);
            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarningItemHeader(CartItemHolderData data) {
        if (data.getCartItemData().isWarning()) {
            tvWarningTitle.setText(data.getCartItemData().getWarningMessageTitle());
            String warningDescription = data.getCartItemData().getWarningMessageDescription();
            if (!TextUtils.isEmpty(warningDescription)) {
                tvWarningDescription.setText(warningDescription);
                tvWarningDescription.setVisibility(View.VISIBLE);
            } else {
                tvWarningDescription.setVisibility(View.GONE);
            }
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private void checkQtyMustDisabled(CartItemHolderData cartItemHolderData, int qty) {
        if ((qty <= QTY_MIN || qty <= cartItemHolderData.getCartItemData().getOriginData().getMinimalQtyOrder()) &&
                (qty >= QTY_MAX || (cartItemHolderData.getCartItemData().getOriginData().getInvenageValue() != 0 &&
                        qty >= cartItemHolderData.getCartItemData().getOriginData().getInvenageValue()))) {
            btnQtyMinus.setEnabled(false);
            btnQtyPlus.setEnabled(false);
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_minus_disabled));
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_plus_disabled));
        } else if (qty <= QTY_MIN || qty <= cartItemHolderData.getCartItemData().getOriginData().getMinimalQtyOrder()) {
            btnQtyMinus.setEnabled(false);
            btnQtyPlus.setEnabled(true);
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_minus_disabled));
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_plus));
        } else if (qty >= QTY_MAX || (cartItemHolderData.getCartItemData().getOriginData().getInvenageValue() != 0 &&
                qty >= cartItemHolderData.getCartItemData().getOriginData().getInvenageValue())) {
            btnQtyPlus.setEnabled(false);
            btnQtyMinus.setEnabled(true);
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_plus_disabled));
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_minus));
        } else {
            btnQtyPlus.setEnabled(true);
            btnQtyMinus.setEnabled(true);
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_plus));
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_button_counter_minus));
        }
    }

    private void validateWithAvailableQuantity(CartItemHolderData data, int qty) {
        if (data.getCartItemData().getOriginData().getInvenageValue() != 0 &&
                qty > data.getCartItemData().getOriginData().getInvenageValue()) {
            String errorMessage = data.getCartItemData().getErrorData().getErrorProductMaxQuantity();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String numberAsString = numberFormat.format(data.getCartItemData().getOriginData().getInvenageValue());
            String maxValue = numberAsString.replace(",", ".");
            tvErrorFormValidation.setText(errorMessage.replace("{{value}}", maxValue));
            tvErrorFormValidation.setVisibility(View.VISIBLE);
        } else if (qty < data.getCartItemData().getOriginData().getMinimalQtyOrder()) {
            String errorMessage = data.getCartItemData().getErrorData().getErrorProductMinQuantity();
            tvErrorFormValidation.setText(errorMessage.replace("{{value}}",
                    String.valueOf(data.getCartItemData().getOriginData().getMinimalQtyOrder())));
            tvErrorFormValidation.setVisibility(View.VISIBLE);
        } else if (qty > QTY_MAX) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String numberAsString = numberFormat.format(data.getCartItemData().getOriginData().getInvenageValue());
            String maxValue = numberAsString.replace(",", ".");
            String errorMessage = data.getCartItemData().getErrorData().getErrorProductMaxQuantity();
            tvErrorFormValidation.setText(errorMessage.replace("{{value}}", maxValue));
            tvErrorFormValidation.setVisibility(View.VISIBLE);
        } else {
            tvErrorFormValidation.setVisibility(View.GONE);
        }
        actionListener.onCartItemAfterErrorChecked();
    }

    private void itemNoteTextWatcherAction(Editable editable) {
        cartItemHolderData.getCartItemData().getUpdatedData().setRemark(editable.toString());
        renderErrorFormItemValidation(cartItemHolderData);
    }

    private void itemQuantityTextWatcherAction(QuantityWrapper quantity) {
        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
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
                    actionListener.onCartItemQuantityReseted(getAdapterPosition(), parentPosition, needToUpdateView);
                    handleRefreshType(cartItemHolderData, viewHolderListener, parentPosition);
                } else if (quantity.getEditable().charAt(0) == '0') {
                    etQty.setText(quantity.getEditable().toString()
                            .substring(zeroCount, quantity.getEditable().toString().length()));
                    etQty.setSelection(etQty.length());
                    needToUpdateView = true;
                }
            } else if (TextUtils.isEmpty(etQty.getText())) {
                actionListener.onCartItemQuantityReseted(getAdapterPosition(), parentPosition,
                        !String.valueOf(quantity.getQtyBefore()).equals(quantity.getEditable().toString()));
                handleRefreshType(cartItemHolderData, viewHolderListener, parentPosition);
            }

            int qty = 0;
            try {
                qty = Integer.parseInt(quantity.getEditable().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            checkQtyMustDisabled(cartItemHolderData, qty);
            cartItemHolderData.getCartItemData().getUpdatedData().setQuantity(qty);
            validateWithAvailableQuantity(cartItemHolderData, qty);
            actionListener.onCartItemQuantityFormEdited(getAdapterPosition(), parentPosition, needToUpdateView);
        }
    }

    public interface ViewHolderListener {

        void onNeedToRefreshSingleProduct(int childPosition);

        void onNeedToRefreshSingleShop(int parentPosition);

        void onNeedToRefreshAllShop();

    }
}
