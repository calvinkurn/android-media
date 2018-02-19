package com.tokopedia.design.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * @author anggaprasetiyo on 19/02/18.
 */
public class AddToCartConfirmationDialog extends DialogFragment {
    public static final String ADD_TO_CART_DIALOG_FRAGMENT_TAG
            = "ADD_TO_CART_DIALOG_FRAGMENT_TAG";
    public static final String ARG_EXTRA_MESSAGE = "ARG_EXTRA_MESSAGE";


    TextView tvMessage;
    TextView btnToCart;
    TextView btnToShopping;

    private String message;
    private ActionListener actionListener;


    public static DialogFragment newInstance(String message) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_EXTRA_MESSAGE, message);
        DialogFragment fragment = new AddToCartConfirmationDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.message = getArguments().getString(ARG_EXTRA_MESSAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_dialog_atc_confirmation, container, false);
        tvMessage = view.findViewById(R.id.tv_message);
        btnToCart = view.findViewById(R.id.btn_go_to_cart);
        btnToShopping = view.findViewById(R.id.btn_dismiss);

        actionListener = (ActionListener) getParentFragment();

        tvMessage.setText(message);
        btnToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onButtonAddToCartPayClicked();
            }
        });
        btnToShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onButtonAddToCartContinueShopingClicked();
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActionListener) {
            actionListener = (ActionListener) context;
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof ActionListener) {
            actionListener = (ActionListener) childFragment;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActionListener) {
            actionListener = (ActionListener) activity;
        }
    }

    public interface ActionListener {
        void onButtonAddToCartPayClicked();

        void onButtonAddToCartContinueShopingClicked();
    }
}
