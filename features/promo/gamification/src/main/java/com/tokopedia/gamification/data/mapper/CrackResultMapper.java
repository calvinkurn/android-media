package com.tokopedia.gamification.data.mapper;

import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.cracktoken.model.CrackButton;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.model.CrackResultStatus;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class CrackResultMapper implements Func1<CrackResultEntity, CrackResult> {

    @Inject
    public CrackResultMapper() {
    }

    @Override
    public CrackResult call(CrackResultEntity crackResultEntity) {
        if (crackResultEntity != null) {
            CrackResult crackResult = new CrackResult();
            
            if (crackResultEntity.getCtaButton() != null) {
                CrackButton ctaButton = new CrackButton();
                ctaButton.setApplink(crackResultEntity.getCtaButton().getApplink());
                ctaButton.setTitle(crackResultEntity.getCtaButton().getTitle());
                ctaButton.setType(crackResultEntity.getCtaButton().getType());
                ctaButton.setUrl(crackResultEntity.getCtaButton().getUrl());
                crackResult.setCtaButton(ctaButton);
            }

            if (crackResultEntity.getReturnButton() != null) {
                CrackButton returnButton = new CrackButton();
                returnButton.setApplink(crackResultEntity.getReturnButton().getApplink());
                returnButton.setTitle(crackResultEntity.getReturnButton().getTitle());
                returnButton.setType(crackResultEntity.getReturnButton().getType());
                returnButton.setUrl(crackResultEntity.getReturnButton().getUrl());
                crackResult.setReturnButton(returnButton);
            }
            
            if (crackResultEntity.getResultStatus() != null) {
                CrackResultStatus crackResultStatus = new CrackResultStatus();
                crackResultStatus.setCode(crackResultEntity.getResultStatus().getCode());
                crackResultStatus.setMessage(crackResultEntity.getResultStatus().getMessage());
                crackResultStatus.setStatus(crackResultEntity.getResultStatus().getStatus());
                crackResult.setResultStatus(crackResultStatus);
            }

            if (crackResultEntity.getBenefits() != null) {
                List<CrackBenefit> crackBenefitList = new ArrayList<>();
                for (CrackBenefitEntity crackBenefitEntity : crackResultEntity.getBenefits()) {
                    CrackBenefit crackBenefit = new CrackBenefit();
                    crackBenefit.setColor(crackBenefitEntity.getColor());
                    crackBenefit.setSize(crackBenefitEntity.getSize());
                    crackBenefit.setBenefitType(crackBenefitEntity.getBenefitType());
                    crackBenefit.setValueBefore(crackBenefitEntity.getValueBefore());
                    crackBenefit.setValueAfter(crackBenefitEntity.getValueAfter());
                    crackBenefit.setTierInformation(crackBenefitEntity.getTierInformation());
                    crackBenefit.setMultiplier(crackBenefitEntity.getMultiplier());
                    crackBenefit.setAnimationType(crackBenefitEntity.getAnimationType());
                    crackBenefit.setTemplateText(crackBenefitEntity.getTemplateText());
                    crackBenefit.setText(crackBenefitEntity.getText());
                    crackBenefitList.add(crackBenefit);
                }
                crackResult.setBenefits(crackBenefitList);
            }

            crackResult.setBenefitType(crackResultEntity.getBenefitType());
            crackResult.setImageUrl(crackResultEntity.getImageUrl());

            return crackResult;
        }
        
        return null;
    }
}
