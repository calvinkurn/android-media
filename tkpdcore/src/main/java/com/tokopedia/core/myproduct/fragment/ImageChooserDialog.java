package com.tokopedia.core.myproduct.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.adapter.ItemImageAdapter;
import com.tokopedia.core.myproduct.adapter.ItemImageAndText;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Toped18 on 8/31/2016.
 */
public class ImageChooserDialog extends DialogFragment {

    public static final String ITEMS = "ITEMS";
    public static final String TAG = "ImageChooserDialog";
    public static final String LISTENER = "LISTENER";


    @BindView(R2.id.title_dialog)
    TextView titleDialog;

    @BindView(R2.id.recycler_view_item_image)
    RecyclerView itemImage;

    @BindView(R2.id.title_cancel)
    TextView cancelDialog;

    @BindView(R2.id.title_confirm)
    TextView confirmDialog;

    private List<? extends ItemImageAndText> items;
    private String title;
    private ItemImageAdapter itemAdapter;
    private SelectWithImage listener;
    private String selectedCatalog;

    public static ImageChooserDialog newInstance(List<? extends ItemImageAndText> items) {
        ImageChooserDialog imageChooserDialog = new ImageChooserDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS, Parcels.wrap(items));
        imageChooserDialog.setArguments(bundle);
        return imageChooserDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = Parcels.unwrap(getArguments().getParcelable(ITEMS));
        }
        if (getActivity() instanceof SelectWithImage) {
            listener = (SelectWithImage) getActivity();
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.item_image_chooser, container, false);
        ButterKnife.bind(this, v);
        itemAdapter = new ItemImageAdapter(items, listener);
        if (selectedCatalog != null)
            itemAdapter.setSelected(selectedCatalog);
        itemImage.setAdapter(itemAdapter);
        itemImage.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (title != null)
            titleDialog.setText(title);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemSelected(itemAdapter.getSelectedItem());
            }
        });
        return v;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelected(String selectedCatalog) {
        this.selectedCatalog = selectedCatalog;
    }

    public interface SelectWithImage {

        void itemSelected(int index);

    }
}
