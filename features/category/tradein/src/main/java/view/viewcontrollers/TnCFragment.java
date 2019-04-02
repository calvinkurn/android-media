package view.viewcontrollers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tradein.R;


public class TnCFragment extends Fragment {

    private int resId;

    public static TnCFragment getInstance(int resid) {
        Bundle bundle = new Bundle();
        bundle.putInt("RESID", resid);
        TnCFragment tnCFragment = new TnCFragment();
        tnCFragment.setArguments(bundle);
        return tnCFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resId = getArguments().getInt("RESID");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_fragment_tnc, container, false);
        TextView tvTnc = rootView.findViewById(R.id.tv_tnc);
        tvTnc.setText(resId);
        return rootView;
    }
}
