package com.tokopedia.home.account.data.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutGroupListItem;
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutListItem;
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse;
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.navigation_common.model.VccUserStatus;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.VccStatus.ACTIVE;
import static com.tokopedia.home.account.AccountConstants.VccStatus.BLOCKED;
import static com.tokopedia.home.account.AccountConstants.VccStatus.DEACTIVATED;
import static com.tokopedia.home.account.AccountConstants.VccStatus.ELIGIBLE;
import static com.tokopedia.home.account.AccountConstants.VccStatus.HOLD;
import static com.tokopedia.home.account.AccountConstants.VccStatus.KYC_PENDING;
import static com.tokopedia.home.account.AccountConstants.VccStatus.REJECTED;

/**
 * @author by alvinatin on 10/08/18.
 */

public class BuyerAccountMapper implements Func1<AccountModel, BuyerViewModel> {
    private static final String OVO = "OVO";
    private static final String OVO_PAY_LATER = "OVO PayLater";
    private static final String LABEL_ELIGIBLE = "Aktifkan";
    private static final String LABEL_HOLD = "Sedang Diproses";
    private static final String LABEL_BLOCKED = "Layanan Terblokir";
    private static final String LABEL_DEACTIVATED = "Dinonaktifkan";
    private static final String LABEL_KYC_PENDING = "Selesaikan Pengajuan Aplikasimu";
    private Context context;
    private RemoteConfig remoteConfig;
    private UserSession userSession;

    @Inject
    BuyerAccountMapper(@ApplicationContext Context context, RemoteConfig remoteConfig, UserSession userSession) {
        this.context = context;
        this.remoteConfig = remoteConfig;
        this.userSession = userSession;
    }

    @Override
    public BuyerViewModel call(AccountModel accountModel) {
        return getBuyerModel(context, accountModel);
    }

    private BuyerViewModel getBuyerModel(Context context, AccountModel accountModel) {
        BuyerViewModel model = new BuyerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        if (accountModel.getProfile() != null) {
            items.add(getBuyerProfileMenu(accountModel));
        }

        String cdnUrl = remoteConfig
                .getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL);

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        if (accountModel.getWallet() != null) {
            tokopediaPayViewModel.setLinked(accountModel.getWallet().isLinked());
            tokopediaPayViewModel.setWalletType(accountModel.getWallet().getWalletType());
            if (accountModel.getWallet().getWalletType().equals(OVO)) {
                tokopediaPayViewModel.setIconUrlLeft(cdnUrl + AccountHomeUrl.ImageUrl.OVO_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    if (accountModel.getWallet().getAmountPendingCashback() > 0) {
                        tokopediaPayViewModel.setLabelLeft("(+" + accountModel.getWallet().getPendingCashback() + ")");
                    } else {
                        tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    }

                    if (accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft("Points " + accountModel.getWallet().getPointBalance());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getCashBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            } else {
                tokopediaPayViewModel.setIconUrlLeft(cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    if (accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            }
        }

        if ((accountModel.getSaldoModel() != null &&
                accountModel.getSaldoModel().getSaldo() != null) ||
                (accountModel.getVccUserStatus() != null && accountModel.getVccUserStatus().getStatus() != null &&
                        accountModel.getVccUserStatus().getStatus().equalsIgnoreCase((AccountConstants.VccStatus.REJECTED)))) {

            tokopediaPayViewModel.setIconUrlRight(cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG);
            tokopediaPayViewModel.setLabelRight(context.getString(R.string.label_tokopedia_pay_deposit));
            tokopediaPayViewModel.setRightSaldo(true);
            if(accountModel.getSaldoModel() != null && accountModel.getSaldoModel().getSaldo() != null) {
                tokopediaPayViewModel.setAmountRight(CurrencyFormatUtil.convertPriceValueToIdrFormat
                        (accountModel.getSaldoModel().getSaldo().getDepositLong(), true));
            }
            tokopediaPayViewModel.setApplinkRight(ApplinkConstInternalGlobal.SALDO_DEPOSIT);
            items.add(tokopediaPayViewModel);

        }

        if (accountModel.getVccUserStatus() != null &&
                accountModel.getVccUserStatus().getTitle() != null &&
                accountModel.getVccUserStatus().getTitle().equalsIgnoreCase(OVO_PAY_LATER)) {
            VccUserStatus vccUserStatus = accountModel.getVccUserStatus();
            TokopediaPayBSModel tokopediaPayBSModel = new TokopediaPayBSModel();

            tokopediaPayViewModel.setIconUrlCentre(vccUserStatus.getIcon());
            tokopediaPayViewModel.setApplinkCentre(vccUserStatus.getRedirectionUrl());

            tokopediaPayViewModel.setAmountCentre(accountModel.getVccUserStatus().getBody());

            switch (vccUserStatus.getStatus()) {
                case ELIGIBLE:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_ELIGIBLE);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case HOLD:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_HOLD);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case ACTIVE:
                    tokopediaPayViewModel.setLabelCentre(vccUserStatus.getTitle());
                    String oplLimit;
                    try {
                        oplLimit = CurrencyFormatUtil.convertPriceValueToIdrFormat(Long.parseLong(vccUserStatus.getBody()), true);
                    } catch (Exception e) {
                        oplLimit = vccUserStatus.getBody();
                    }
                    tokopediaPayViewModel.setAmountCentre(oplLimit);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case REJECTED:
                    tokopediaPayViewModel.setLabelCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setBsDataCentre(null);
                    break;
                case BLOCKED:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_BLOCKED);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case DEACTIVATED:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_DEACTIVATED);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case KYC_PENDING:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_KYC_PENDING);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;

            }

        } else {
            tokopediaPayViewModel.setBsDataCentre(null);
        }
        items.addAll(StaticBuyerModelGenerator.Companion.getModel(context, accountModel, remoteConfig));
        model.setItems(items);

        return model;
    }

    private BuyerCardViewModel getBuyerProfileMenu(AccountModel accountModel) {
        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setUserId(accountModel.getProfile().getUserId());
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());
        ShortcutResponse shortcutResponse = accountModel.getShortcutResponse();
        if (shortcutResponse!=null) {
            ShortcutGroupListItem shortcutListItem = null;
            ArrayList<ShortcutListItem> shortcutListItems = new ArrayList<>();

            if (shortcutResponse.getTokopointsShortcutList() != null &&
                    shortcutResponse.getTokopointsShortcutList().getShortcutGroupList() != null &&
                    shortcutResponse.getTokopointsShortcutList().getShortcutGroupList().size() != 0) {
                shortcutListItem = shortcutResponse.getTokopointsShortcutList().getShortcutGroupList().get(0);
                shortcutListItems = (ArrayList<ShortcutListItem>) shortcutListItem.getShortcutList();
            }

            if (shortcutListItems != null && shortcutListItems.size() > 0) {

                buyerCardViewModel.setTokopointSize(1);
                buyerCardViewModel.setTokopointTitle(shortcutListItems.get(0).getCta().getText());
                buyerCardViewModel.setTokopoint(shortcutListItems.get(0).getDescription());
                buyerCardViewModel.setTokopointAppplink(shortcutListItems.get(0).getCta().getAppLink());
                buyerCardViewModel.setTokopointImageUrl(shortcutListItems.get(0).getIconImageURL());

                if (shortcutListItems.size() > 1) {
                    buyerCardViewModel.setCouponSize(1);
                    buyerCardViewModel.setCouponTitle(shortcutListItems.get(1).getCta().getText());
                    buyerCardViewModel.setCoupons(shortcutListItems.get(1).getDescription());
                    buyerCardViewModel.setCouponApplink(shortcutListItems.get(1).getCta().getAppLink());
                    buyerCardViewModel.setCouponImageUrl(shortcutListItems.get(1).getIconImageURL());
                }

                if (shortcutListItems.size() > 2) {
                    buyerCardViewModel.setTokomemberSize(2);
                    buyerCardViewModel.setTokomemberTitle(shortcutListItems.get(2).getCta().getText());
                    buyerCardViewModel.setTokomember(shortcutListItems.get(2).getDescription());
                    buyerCardViewModel.setTokomemberApplink(shortcutListItems.get(2).getCta().getAppLink());
                    buyerCardViewModel.setTokomemberImageUrl(shortcutListItems.get(2).getIconImageURL());
                }
            }
        }
            buyerCardViewModel.setEggImageUrl(accountModel.getTokopoints().getStatus().getTier().getImageUrl());
            buyerCardViewModel.setMemberStatus(accountModel.getTokopoints().getStatus().getTier().getNameDesc());
            buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
            buyerCardViewModel.setProgress(accountModel.getUserProfileCompletion().getCompletionScore());

            buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
            if (accountModel.getProfile().getCompletion() != null) {
                buyerCardViewModel.setProgress(accountModel.getUserProfileCompletion().getCompletionScore());
            }
            buyerCardViewModel.setAffiliate(accountModel.isAffiliate());
        userSession.setHasPassword(accountModel.getUserProfileCompletion().isCreatedPassword());
        return buyerCardViewModel;
    }
}
