---
title: "Mini Consultation"
labels:
- webview
- chat-dokter
- epharmacy
- mini-consultation
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| **Project Lead** | [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence)  |
| Product Manager | [Elison Koenaifi](https://tokopedia.atlassian.net/wiki/people/70121:221c7b73-3301-416b-ab69-016ae76422b8?ref=confluence) [Siti Kaamiliaa Hasna](https://tokopedia.atlassian.net/wiki/people/61daf5f50586a20069921055?ref=confluence)  |
| Team | [Darth](https://tokopedia.atlassian.net/people/team/8c90de56-d4f1-45a7-9021-bd87c4ea9ce2) ([Mohit Singh](https://tokopedia.atlassian.net/wiki/people/5ff3fa2244065f013f9f1eb9?ref=confluence) ) |
| Release date | 24 Aug 2023 / <!--start status:GREY-->MA-3.209<!--end status--> <!--start status:GREY-->SA-2.139<!--end status-->  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | PRD: <https://docs.google.com/document/d/1k98JwICbstj55R287ggxh85D2n_rS7vBLXP2vi-JadY/edit?pli=1>  |
| Module Location | `features/category/epharmacy` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:24 Feb 2023 (MA-3.209/SA-2.139)-->
Mini Consultation First release
<!--end expand-->

<!--start expand:3 Mar 2023 (MA-3.210/SA-2.140)-->
Halodoc fix in web view .
<!--end expand-->

## Overview

Mini Consultation page aggregates all the checked out products and groups them into respective Medical Partner Groups i.e Halodoc, Dkonsul for online consultation and prescriptions.

User can then consult and get prescription attached. The prescription can then be viewed.

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.23.32%20PM.png)

### Background

Through [EPharmacy Upload Prescription](/wiki/spaces/PA/pages/2106131465/E-Pharmacy), the user already had to have the prescription with them. So they can upload that. In this we missed all those people who did not have prescriptions with them already and wanted to purchase ethical products.

With Mini Consultation we provide the facility with the help of partnership with various companies to consult a doctor online and provide the prescriptions. The prescription attaches itself and can be viewed by the user.

### Project Description

Tokopedia has partnered with 5 pharmacy aggregators (GoApotik, Halodoc, KlikDokter, Century, Pharmanet) to onboard 1800+ pharmacies since 2019. We have helped buyers to get access to ethical medicines without leaving their home and have processed over 47 Bio worth of monthly transactions for partnered pharmacies alone. However, in November 2021 we received a warning both from the Indonesian government and Google due to non compliance with the regulation which requires every online ethical medicine purchase to have a prescription before payment. Right now Tokopedia enables prescription medicine sales by asking the buyer to upload their prescription via Notif Center after the payment is made.

## Tech Stack

- Media Picker : Added Media Picker to Web View
- Web View : Opening partner site for consultation

## Flow Diagram

[![](../res/mini-consultation/EP%20MC.drawio.png)](https://app.diagrams.net/#G1Kkc2uOCDAHncUZlcfnB3hgwxrGbtcygU)![](res/Epharmacy%20Web%20view.drawio%20%281%29.png)

## Navigation

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.23.32%20PM.png)

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.23.56%20PM.png)

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.24.14%20PM.png)

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.24.54%20PM.png)

![](../res/mini-consultation/Screenshot%202023-02-21%20at%202.25.13%20PM.png)

## How-to



| **API** | **Notes** | **Type** |
| --- | --- | --- |
| [Prepare Products Group](/wiki/spaces/CT/pages/2053540381/Prepare+Products+Group)  | Fetch all the groups of shops grouped by partners | GQL |
| [Initiate Consultation](/wiki/spaces/CT/pages/2053738213/Initiate+Consultation)  | Used for initiating consultation and fetching the link to redirect for consultation i.e `pwa_link` | GQL |
| [GetConsultationDetails](/wiki/spaces/CT/pages/2054719579/GetConsultationDetails)  | Used for fetching document url for prescription viewing.  | GQL |
| [Epharmacy Static Data](/wiki/spaces/CT/pages/2063142203/Epharmacy+Static+Data)  | Get Static data to populate Obat keras info Bottom Sheet and Obat keras TNC bottom sheet | GQL |
| [Epharmacy User Reminder](/wiki/spaces/CT/pages/2091484577/Epharmacy+User+Reminder)  | Set reminder by user for EPharmacy | GQL |

AppLinks :

tokopedia://epharmacy/edu/${partner\_name}/obat\_keras\_tnc  
tokopedia://epharmacy/edu/${partner\_name}/obat\_keras\_info

tokopedia://epharmacy/attach-prescription/



---

## Useful Links

- [Figma](https://figma.com/)

