---
title: Edit Shipping
labels:
- logistic
- edit-shipping
---

<!--left header table-->
| Status          | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                         |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors    | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) [Irpan](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence)  |
| Product Manager | [Aditya Rifaldi](https://tokopedia.atlassian.net/wiki/people/603c7cf8333ff40070ba5f3c?ref=confluence)                                                                                                                                                                                                     |
| Team            | Minion Bob                                                                                                                                                                                                                                                                                                |
| Release date    | -                                                                                                                                                                                                                                                                                                         |
| Module type     | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                        |
| Product PRD     | -                                                                                                                                                                                                                                                                                                         |
| Module Location | `features/logistic/editshipping`                                                                                                                                                                                                                                                                          |

## Background

Tokopedia facilitates seller to set up courier with a feature called `Shipping Editor`. Seller can set up courier for shop level wide or just for a specific product. 

## Description

Shipping editor page is a page to show available courier that can be chosen by seller. There are two different shipping editors based on number of seller warehouse. If seller only support one warehouse, then system will direct seller to shipping editor single loc page. If seller support more than one warehouse, system will direct seller to shipping editor multi loc page. Seller can also setup courier for a specific product only in Custom Product Logistic (CPL) page.

![](res/description.png)

## Sub-features / Widget / Interactions

- [Shipping Editor](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1761280076/Shipping+Editor)
  - Single Loc
  - Multi Loc
- [Custom Product Logistic](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2103097544/Custom+Product+Logistic)
- [Edit Address Multi Location](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2106130969/Edit+Shipping+Edit+Shop+Multilocation+Address)