---
title: Logistic Seller
labels:
- logistic
- logistic-seller
---

<!--left header table-->
| Contributors    | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Team            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Module type     | ​<!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                        |
| Module Location | `features/logistic/logisticseller`                                                                                                                                                                                                                                                                         |

<!--toc-->

## Overview

### Project Description

Logistic Seller is a feature from the logistics side that is used for sellers which can be accessed via the SOM ([Seller Order Management](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1225515219/SOM+Revamp+2020)) page. There are several features for reschedule pickup, return to shipper, and finding new driver.

- [Logistic Seller: Reschedule Pickup](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2109178143/Logistic+Seller%3A+Reschedule+Pickup) <!--start status:GREEN-->RELEASE<!--end status-->
- [Logistic Seller: Return To Shipper](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2107638120/Logistic+Seller%3A+Return+To+Shipper) <!--start status:GREEN-->RELEASE<!--end status-->
- [Logistic Seller: Finding New Driver](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2110980180/Logistic+Seller%3A+Finding+New+Driver) <!--start status:YELLOW-->ON TESTING<!--end status-->

### Entry Point

Entry points for feature on Logistic Seller can accessed from SOM List & Som Detail

- Reschedule Pickup : [SomDetailFragment](https://github.com/tokopedia/android-tokopedia-core/blob/4926bee04db7b922185fecd932607b6a0794c46b/features/transaction/sellerorder/src/main/java/com/tokopedia/sellerorder/detail/presentation/fragment/SomDetailFragment.kt#L833)
- Return To Shipper : [SomDetailFragment](https://github.com/tokopedia/android-tokopedia-core/blob/4926bee04db7b922185fecd932607b6a0794c46b/features/transaction/sellerorder/src/main/java/com/tokopedia/sellerorder/detail/presentation/fragment/SomDetailFragment.kt#L837) & [SomListFragment](https://github.com/tokopedia/android-tokopedia-core/blob/4926bee04db7b922185fecd932607b6a0794c46b/features/transaction/sellerorder/src/main/java/com/tokopedia/sellerorder/list/presentation/fragments/SomListFragment.kt#L725)
- Finding New Driver : [SomDetailFragment](https://github.com/tokopedia/android-tokopedia-core/blob/7abe417b672afc863f8fea1cd23edc2c555d868c/features/transaction/sellerorder/src/main/java/com/tokopedia/sellerorder/detail/presentation/fragment/SomDetailFragment.kt#L825)