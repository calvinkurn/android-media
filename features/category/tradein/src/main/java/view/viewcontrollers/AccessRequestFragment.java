package view.viewcontrollers;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein.R;

import viewmodel.AccessRequestViewModel;
import viewmodel.IAccessRequestListener;

public class AccessRequestFragment extends DialogFragment {

    public static final String TAG = "ACCESS REQUEST FRAGMENT";

    private IAccessRequestListener accessRequestListener;

    public static AccessRequestFragment newInstance() {
        AccessRequestFragment accessRequestFragment = new AccessRequestFragment();
        return accessRequestFragment;
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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.permission_fragment);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        accessRequestListener = (IAccessRequestListener) activity;
    }

   public class AccessRequestClickListener implements View.OnClickListener {

       @Override
       public void onClick(View v) {
           if (v.getId() == R.id.button_accept) {
               sendGeneralEvent("setuju");
               accessRequestListener.clickAccept();
               dismiss();
           } else {
               sendGeneralEvent("batal");
               dismiss();
           }
       }

       private void sendGeneralEvent(String label) {
           TrackApp trackApp = TrackApp.getInstance();
           trackApp.getGTM().sendGeneralEvent("clickPDP",
                   "product detail page",
                   "click - asking permission trade in",
                   label);
       }
   }

}
