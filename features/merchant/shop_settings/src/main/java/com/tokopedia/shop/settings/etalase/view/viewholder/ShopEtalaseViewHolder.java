package com.tokopedia.shop.settings.etalase.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.common.util.SpanTextUtil;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseViewHolder extends AbstractViewHolder<ShopEtalaseViewModel> {

    public static final int LAYOUT = R.layout.item_shop_settings_etalase;

    private View ivMenuMore;
    private TextView tvEtalaseName;
    private TextView tvEtalaseCount;
    private ForegroundColorSpan boldColor;

    public ShopEtalaseViewHolder(View itemView,
                                 OnShopEtalaseViewHolderListener onOnShopEtalaseViewHolderListener) {
        super(itemView);
        this.onOnShopEtalaseViewHolderListener = onOnShopEtalaseViewHolderListener;
        ivMenuMore = itemView.findViewById(R.id.ivMenuMore);
        tvEtalaseName = itemView.findViewById(R.id.tvEtalaseName);
        tvEtalaseCount = itemView.findViewById(R.id.tvEtalaseCount);
        boldColor = new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), R.color.font_black_primary_70));
    }

    private OnShopEtalaseViewHolderListener onOnShopEtalaseViewHolderListener;
    public interface OnShopEtalaseViewHolderListener {
        void onIconMoreClicked(ShopEtalaseViewModel shopEtalaseViewModel);
        String getKeyword();
    }

    @Override
    public void bind(ShopEtalaseViewModel shopEtalaseViewModel) {
        String keyword = "";
        if (onOnShopEtalaseViewHolderListener != null) {
            keyword = onOnShopEtalaseViewHolderListener.getKeyword();
        }
        tvEtalaseName.setText(SpanTextUtil.getSpandableColorText(shopEtalaseViewModel.getName(),keyword, boldColor));
        tvEtalaseCount.setText(tvEtalaseCount.getContext().getString(R.string.x_products, shopEtalaseViewModel.getCount()));
        if (shopEtalaseViewModel.getType() == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
            ivMenuMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOnShopEtalaseViewHolderListener != null) {
                        onOnShopEtalaseViewHolderListener.onIconMoreClicked(shopEtalaseViewModel);
                    }
                }
            });
            ivMenuMore.setVisibility(View.VISIBLE);
        } else { // etalase default cannot be editted/deleted
            ivMenuMore.setVisibility(View.GONE);
        }
    }

}
