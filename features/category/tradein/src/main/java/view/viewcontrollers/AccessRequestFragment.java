package view.viewcontrollers;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tradein.R;

import viewmodel.AccessRequestViewModel;
import viewmodel.IAccessRequestListener;

public class AccessRequestFragment extends DialogFragment {

    public static final String TAG = "ACCESS REQUEST FRAGMENT";

    private AccessRequestViewModel mViewModel;

    private int newPrice;

    private IAccessRequestListener accessRequestListener;

    public static AccessRequestFragment newInstance(int price) {
        Bundle bundle = new Bundle();
        bundle.putInt("NEW PRICE",price);
        AccessRequestFragment accessRequestFragment = new AccessRequestFragment();
        accessRequestFragment.setArguments(bundle);
        return accessRequestFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        newPrice = getArguments().getInt("NEW PRICE");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.permission_fragment, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView buttonAccept = view.findViewById(R.id.button_accept);
        TextView buttonDeny = view.findViewById(R.id.button_deny);
        AccessRequestClickListener clickListener = new AccessRequestClickListener();
        buttonAccept.setOnClickListener(clickListener);
        buttonDeny.setOnClickListener(clickListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccessRequestViewModel.class);
        accessRequestListener = mViewModel;
        mViewModel.getPermissionLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isAllow) {
                if (isAllow != null && isAllow) {
                    Intent tradeInHomeIntent = new Intent(getContext(), TradeInHomeActivity.class);
                    tradeInHomeIntent.putExtra("NEW PRICE",newPrice);
                    startActivityForResult(tradeInHomeIntent, 1001);
                } else
                    dismiss();
            }
        });
    }

    public class AccessRequestClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_accept)
                accessRequestListener.clickAccept();
            else
                accessRequestListener.clickDeny();
        }
    }

}
