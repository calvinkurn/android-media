package view.viewcontrollers;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tradein.R;

import viewmodel.AccessRequestViewModel;
import viewmodel.IAccessRequestListener;

public class AccessRequestFragment extends DialogFragment {

    AccessRequestViewModel mViewModel;

    private IAccessRequestListener accessRequestListener;

    public static AccessRequestFragment newInstance() {
        return new AccessRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.permission_fragment, container, false);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccessRequestViewModel.class);
        accessRequestListener = mViewModel;
        mViewModel.getPermissionLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isAllow) {
                if (isAllow != null && isAllow) {
                    startActivityForResult(new Intent(getContext(), TradeInHomeActivity.class), 1001);
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
