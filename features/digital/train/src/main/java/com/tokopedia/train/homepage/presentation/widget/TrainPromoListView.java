package com.tokopedia.train.homepage.presentation.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.homepage.presentation.TrainPromoAdapter;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;

import java.util.List;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainPromoListView extends FrameLayout {

    private static final int HORIZONTAL = 0;

    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_TRAIN =
            "CLIP_DATA_LABEL_VOUCHER_CODE_TRAIN";

    private static final float DP_16 = 16;

    private TextView seeAllPromo;
    private RecyclerView recyclerViewPromo;
    private TrainPromoAdapter adapter;
    private RelativeLayout layoutTrainPromo;
    private Context context;
    private ActionListener listener;

    public TrainPromoListView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TrainPromoListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TrainPromoListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_train_promo, this, true);
        seeAllPromo = view.findViewById(R.id.see_all_promo);
        recyclerViewPromo = view.findViewById(R.id.recycler_view_train_promo);
        layoutTrainPromo = view.findViewById(R.id.layout_train_promo);

        seeAllPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.navigateToPromoPage();
            }
        });

        initializedLayout();
    }

    private void initializedLayout() {
        adapter = new TrainPromoAdapter();
        adapter.setListener(new TrainPromoAdapter.ListenerTrainPromoAdapter() {
            @Override
            public void onClickCopyPromoCode(String promoCode) {
                copyPromoCode(promoCode);
            }

            @Override
            public void onClickItemPromo(int position) {
                listener.navigateToPromoDetail(position);
            }
        });
        recyclerViewPromo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPromo.setAdapter(adapter);
        if (context.getApplicationContext() instanceof TrainRouter) {
            recyclerViewPromo.addItemDecoration(((TrainRouter) context.getApplicationContext())
                    .getSpacingItemDecorationHome(convertDpToPixel(DP_16, context), HORIZONTAL));
            SnapHelper snapHelper = ((TrainRouter) context.getApplicationContext()).getSnapHelper();
            snapHelper.attachToRecyclerView(recyclerViewPromo);
        }
    }

    public void setPromoListData(List<TrainPromoViewModel> trainPromoViewModelList) {
        layoutTrainPromo.setVisibility(View.VISIBLE);
        adapter.addItems(trainPromoViewModelList);
    }

    public void hidePromoList() {
        layoutTrainPromo.setVisibility(View.GONE);
    }

    private void copyPromoCode(String promoCode) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                CLIP_DATA_LABEL_VOUCHER_CODE_TRAIN, promoCode
        );
        clipboard.setPrimaryClip(clip);
        listener.showToastMessage(context.getString(R.string.message_voucher_code_banner_copied));
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public void showPromoList() {
        layoutTrainPromo.setVisibility(View.VISIBLE);
    }

    public interface ActionListener {
        void showToastMessage(String message);

        void navigateToPromoPage();

        void navigateToPromoDetail(int position);
    }
}
