import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  StyleSheet, TextInput, View, KeyboardAvoidingView, Button, Image, ScrollView, Picker,
  Dimensions, TouchableOpacity, TouchableWithoutFeedback, TouchableNativeFeedback, ListView
} from 'react-native';
import PopupDialog, { DialogTitle } from 'react-native-popup-dialog';
import { StackNavigator } from 'react-navigation';
import { getBankList, selectBank, getEmiList, selectEmi, makePayment, reloadState } from '../../../actions/index';
import { Text } from '../../../common/TKPText'
import { NavigationModule } from 'NativeModules'
import numeral from 'numeral'
import { icons } from '../../../lib/config'



class PaymentBank extends Component {
  constructor(props) {
    super(props);
    const dsEmi = new ListView.DataSource({
          rowHasChanged: (r1, r2) => r1 !== r2 });

    this.state = {
      selectedBank: null,
      selectIdBank: null,
      selectedEmiId: null,
      selectedBankData: {},
      otherBank: null,
      paymentMethod: null,
      emiList: dsEmi.cloneWithRows([{
        term: 0,
        text: "Tanpa Cicilan",
        available: false
      }]),
      dsEmi: dsEmi,
      installmentCalc: 0,
      noInstallmentCalc: 0,
    };
  }

  componentDidMount() {
    this.props.dispatch(getBankList());
    this.props.navigation.setParams({ dispatch: this.props.dispatch });
  }


  _handleButtonPress = () => {
    if(this._isInputValid()) {
      const selected_installment = !this.state.selectedEmiId ? 0 : (this.state.selectedEmiId)
      const selectedBankData = !this.state.selectedBankData ? {} : this.state.selectedBankData
      const checkout_data = JSON.parse(this.props.screenProps.checkout_data)
      const payment_amount = checkout_data.data.data.payment_amount

      const { 
        noInstallmentCalc, 
        installmentCalc, 
        selectedEmiId,
        selectedBank,
        selectIdBank,
      } = this.state


      if (this.state.paymentMethod === 'SCAN'){
        const data_scan_params = {
          checkout_data: this.props.screenProps.checkout_data,
          selectBank: selectedBank,
          selectIdBank: selectIdBank,
          selectedEmiId: selected_installment,
          selectedBankData: selectedBankData,
          total_payment: this.props.screenProps.total_payment
        }
        // console.log(data_scan_params)
        // console.log(this.props.screenProps)
        // NavigationModule.navigate("posapp://payment/scan", JSON.stringify(data_scan_params))

      } else {
        // console.log(selectedEmiId)
        // // console.log(selectEmi)
        // console.log(installmentCalc)
        // console.log(noInstallmentCalc)
        // console.log(payment_amount)
        const payment_with_rates = selectedEmiId ? payment_amount : noInstallmentCalc 
        // console.log(payment_with_rates)
        // console.log(payment_amount)

        this.props.navigation.navigate('Payment', {
          checkout_data: this.props.screenProps.checkout_data,
          selectBank: selectedBank,
          selectIdBank: selectIdBank,
          selectedEmiId: selected_installment,
          selectedBankData: selectedBankData,
          total_payment: payment_amount,
          total_payment_with_rates_or_fees: payment_with_rates
          // total_payment_with_rates_or_fees: selectedEmiId ? payment_with_rates : noInstallmentCalc,
        })
      }
    }
  };

  _isInputValid(selectedBankData) {
      if(!this.state.selectedBankData) {
        // console.log("bank not selected")
        return false
      }

      // console.log(this.state.selectedEmiId)
      if(this.state.selectedEmiId === null) {
        // console.log("emi not selected")
        return false
      }

      if(!this.state.paymentMethod) {
        // console.log("payment method not selected")
        return false
      }

      return true
  }



  _renderPopRow(rowData, sectionID, rowID) {
    if (rowID > 10) {
      return (
        <TouchableOpacity onPress={this._onPressPopupRow.bind(this, rowID, rowData)}>
          <View style={styles.popupRow}>
            <View style={styles.popupCol}>
              <Image style={styles.popupImage} source={{uri: rowData.bank_logo}} />
            </View>
            <View style={styles.popupCol}>
              <Text style={styles.popupText}> {rowData.bank_name} </Text>
            </View>
          </View>
        </TouchableOpacity>
      );
    }

    return (null);
  }


  _renderEmiRow(rowData, sectionID, rowID) {
    if (rowData.available === false) {
      return (
        <TouchableOpacity onPress={this._onPressEmiRow.bind(this, rowID, rowData)}>
          <View style={[styles.emiBox, { marginRight: 10, height: 80 }, this.state.selectedEmiId === rowData.term ? styles.selectedBorder : {}]}>
            <Text style={styles.emiText}>{rowData.text}</Text>
          </View>
        </TouchableOpacity>
      );
    }

    return (
      <TouchableOpacity onPress={this._onPressEmiRow.bind(this, rowID, rowData)}>
        <View style={[styles.emiBox, { marginRight: 10, height: 80 }, this.state.selectedEmiId === rowData.term ? styles.selectedBorder : {}]}>
          <Text style={styles.emiText}>{rowData.term} Bulan</Text>
        </View>
      </TouchableOpacity>
    );
  }


  _renderBankLogo(rowData, sectionID, rowID) {
    // console.log(rowData,sectionID, rowID)
    if (rowID <= 10) {
      return (
        <TouchableOpacity onPress={this._onPressLogo.bind(this, rowID, rowData)}>
          <View style={[styles.logoBox, { marginTop: 10, height: 80 }, (rowData.isSelected ? styles.selectedBorder : '')]}>
            <Image source={{ uri: rowData.bank_logo || 'http://via.placeholder.com/50x27' }}
              style={styles.cardLogo} />
          </View>
        </TouchableOpacity>
      );
    } else if (rowID <= 11) {
      return (
        <TouchableOpacity style={[styles.logoBox, { marginTop: 10, height: 80 }]}
          onPress={() => { this.popupDialog.show() }} >
          <View>
            <Text style={{ textAlign:'center', fontSize: 13, color: '#0000008a' }}>Bank{"\n"}Lainnya</Text>
          </View>
        </TouchableOpacity>
      );
    } else if (rowID < 99) {
      return <View style={[styles.logoBox, styles.noBorder]} />
    }

    return (<View style={{ height: 0 }} />);
  }



  _onPressPopupRow(rowID, rowData) {
    let emiList = this.state.dsEmi.cloneWithRows([{
      term: 0,
      text: "Tanpa Cicilan",
      available: false
    }]);

    if (rowData.allow_installment) {
     emiList = this.state.dsEmi.cloneWithRows([...[{
          text: "Tanpa Cicilan",
          available: false
        }], ...rowData.installment_list]);
    }

    this.setState({
      selectedBank: rowData.bank_name,
      otherBank: rowData.bank_logo,
      selectedEmiId: null,
      selectedBankData:rowData,
      emiList: emiList
    }, () => {
      this.props.dispatch(selectBank(rowData.bank_id));
      this.popupDialog.dismiss();
    });
  }

  _onPressLogo(rowID, rowData) {
    if (!rowData.isSelected) {
      let emiList = this.state.dsEmi.cloneWithRows([{
        text: "Tanpa Cicilan",
        available: false
      }]);

      if (rowData.allow_installment) {
         emiList = this.state.dsEmi.cloneWithRows([...[{
              text: "Tanpa Cicilan",
              available: false
            }], ...rowData.installment_list]);
      }

      this.setState({
        selectedBank: rowData.bank_name,
        selectIdBank: rowData.bank_id,
        otherBank: null,
        selectedEmiId: null,
        selectedBankData:rowData,
        emiList: emiList
      });

      this.props.dispatch(selectBank(rowData.bank_id));
    }
  }

  _onPressEmiRow(rowID, rowData) {
    const checkout_data = JSON.parse(this.props.screenProps.checkout_data)
    const payment_amount = checkout_data.data.data.payment_amount

    const { paymentRate_CreditCardInterestRate,
      paymentRate_CreditCardFee,
      paymentRate_CreditCardFlag,
      paymentRate_InstallmentInterestRate,
      paymentRate_InstallmentFee,
      paymentRate_InstallmentFlag,
      paymentRate_errorMessage,
      paymentRate_isFetching } = this.props
    // console.log(rowID, rowData)
    // console.log(this.props)
    // console.log(payment_amount)
    // console.log(paymentRate_CreditCardInterestRate)

    this.setState({ selectedEmiId: rowData.term })

    // console.log(rowData.term)

    
    if (!rowData.term){       // Without cicilan
      if (paymentRate_CreditCardFlag === 1){
        this.setState({
          noInstallmentCalc: payment_amount + ( paymentRate_CreditCardInterestRate / 100 * payment_amount )
        })    
      }
      if (paymentRate_CreditCardFlag === 2){
        this.setState({
          noInstallmentCalc: ( payment_amount + paymentRate_CreditCardFee ) * payment_amount
        })    
      }
    } else {    // With Cicilan
      if (paymentRate_CreditCardFlag === 1){
        const cicilan_amount = ( payment_amount + paymentRate_InstallmentInterestRate / 100 ) / rowData.term 
        this.setState({
          installmentCalc: Math.ceil(cicilan_amount)
        })
      }
      if (paymentRate_CreditCardFlag === 2){
        this.setState({
          installmentCalc: ( payment_amount / rowData.term ) + paymentRate_InstallmentFee
        })
      }
    }
  }


  static navigationOptions = ({navigation}) => ({
    title: 'Pembayaran',
    headerTintColor: '#FFF',
    headerStyle: {
        backgroundColor: '#42B549'
    },
    headerLeft: (
      <TouchableOpacity onPress={() => PaymentBank.onBackPress(navigation.state.params)}>
        <Image source={{ uri: icons.arrow_back }} style={{ width: 15, height: 15, margin: 20, resizeMode: 'contain' }} />
      </TouchableOpacity>
    )
  })


  static onBackPress(props) {
    props.dispatch(reloadState('PaymentBank'))
    NavigationModule.finish()
  }

  _choosePaymentMethod(paymentMethod) {
    this.setState({
      paymentMethod
    })
  }

  render() {
    const checkout_data = JSON.parse(this.props.screenProps.checkout_data)
    const payment_amount = checkout_data.data.data.payment_amount
    // const payment_amount_curr = (checkout_data.data.data.payment_amount).toLocaleString("id") || ''
    const totalPriceWithCurrency = numeral(payment_amount).format('0,0') || ''
    const { installmentCalc, noInstallmentCalc } = this.state
    // console.log(installmentCalc, typeof installmentCalc, noInstallmentCalc, typeof noInstallmentCalc)
    const installmentCalcWithCurrency = installmentCalc === 0 ? numeral(payment_amount).format('0,0') : numeral(installmentCalc).format('0,0')
    const noInstallmentCalcWithCurrency = noInstallmentCalc === 0 ? numeral(payment_amount).format('0,0') : numeral(noInstallmentCalc).format('0,0')
    
    // console.log(payment_amount)
    // console.log(checkout_data)
    // console.log(installmentCalcWithCurrency)
    // console.log(noInstallmentCalcWithCurrency)



    return (
      <View style={styles.mainContainers} >
        {/* <View style={styles.header}>
          <Button
            title="Go to Payment page ->"
            color="#42B549"
            onPress={() =>
              this.props.navigation.navigate('Payment', {})
            }
          />
        </View> */}
            <ScrollView>
            <View style={styles.containers} >
              <View style={[styles.row, styles.row1]} >
                <Text style={[styles.font20Bold, styles.fontColor70]}>Total Pembayaran</Text>
                <Text style={[styles.font20Bold, styles.fontColor70]}>Rp {(totalPriceWithCurrency)}</Text>
              </View>

              <View style={[styles.row, styles.row2]} >
                <Text style={[styles.font16, styles.fontColor70]}>Minimum Purchase Rp 500.000</Text>
              </View>

              <View style={[styles.row, styles.row3]} >
                <Text style={[styles.font18Bold, styles.fontColor70]}>Pilih Bank</Text>
              </View>

              <ListView
                contentContainerStyle={[{ flexDirection: 'row', backgroundColor: '#fff', borderBottomWidth: 0, flexWrap: 'wrap', paddingLeft: 10, paddingTop: 10, paddingBottom: 10 }]}
                dataSource={this.props.bankList}
                enableEmptySections={true}
                initialListSize={12}
                renderRow={this._renderBankLogo.bind(this)} />
              {
                this.state.otherBank ?
                  (
                    <View style={{ backgroundColor: '#fff', paddingHorizontal: 10 }}>
                      <View style={[styles.logoBox, { marginTop: 10, height: 80 }, styles.selectedBorder]}>
                        <Image source={{uri: this.state.otherBank}} style={styles.cardLogo} />
                      </View>
                    </View>
                  ) :
                  null
              }

              {
                (this.state.selectedBank === null) ? null :
                  (
                    <View style={{ alignItems: 'flex-end', paddingHorizontal: 15, backgroundColor: '#fff' }}>
                      <Text style={[styles.font16, styles.fontColor70]}>
                        Anda memilih {this.state.selectedBank}
                      </Text>
                    </View>
                  )
              }


              <View style={[styles.row, styles.row3]} >
                <Text style={[styles.font18Bold, styles.fontColor70]}>
                  Cicilan Kartu Kredit Bunga 0%
                </Text>
              </View>

              <ListView
                contentContainerStyle={{ paddingTop: 20, flexDirection: 'row', backgroundColor: '#fff', paddingLeft: 15 }}
                dataSource={this.state.emiList}
                enableEmptySections={true}
                renderRow={this._renderEmiRow.bind(this)} />

              <View style={{ paddingTop: 10, backgroundColor: 'white', borderBottomWidth: 1, borderColor: '#F0F0F0' }}>
                <Text style={[styles.font18Bold, styles.fontColor70, { marginLeft: 15 }]}>Pilih Metode Pembayaran</Text>
                <View style={{ paddingVertical: 20, flexDirection: 'row', marginLeft: 15 }}>
                  <TouchableWithoutFeedback 
                    disabled={true}
                    onPress={this._choosePaymentMethod.bind(this, 'SCAN')}>
                    <View style={[styles.paymentMethod, { marginRight: 10 }, (this.state.paymentMethod === 'SCAN') ? styles.paymentSelected : {}]}>
                      <Text style={[styles.emiText]}>Scan Kartu Kredit</Text>
                    </View>
                  </TouchableWithoutFeedback>

                  <TouchableWithoutFeedback onPress={this._choosePaymentMethod.bind(this, 'ONLINE')}>
                    <View style={[styles.paymentMethod, (this.state.paymentMethod === 'ONLINE') ? styles.paymentSelected : {}]}>
                      <Text style={[styles.emiText]}>Pembayaran Online</Text>
                    </View>
                  </TouchableWithoutFeedback>
                </View>
              </View>

              
              <View style={[styles.row, styles.row1]}>
                <Text style={[styles.font20Bold, styles.fontColor70]}>Nominal</Text>
                {payment_amount && this.state.selectedEmiId ?  
                  (<Text style={[styles.font20Bold, styles.fontColor70]}>
                    Rp {installmentCalcWithCurrency}/ bulan</Text>): (
                      <Text style={[styles.font20Bold, styles.fontColor70]}>
                        Rp {noInstallmentCalcWithCurrency}</Text>)
                }
              </View>
              {/*<View style={[styles.row, styles.row3]} >
                  <Text style={styles.row1Text}>
                    Pilih Metode Pembayaran
                  </Text>
                </View>
                <View style={[styles.row, styles.row1]} >
                  <Text>
                    Nominal
                  </Text>
                  <Text style={styles.row1Text}>
                    Rp 2.266.334/bulan
                  </Text>
                </View>*/}

              <View style={styles.buttonContainer}>
                {/*<Button
                    style={{height:52}}
                    onPress = {this._handleButtonPress}
                    title="Bayar"
                    color='#FF5722'
                  />*/}
                <TouchableNativeFeedback
                  onPress={this._handleButtonPress}
                  background={TouchableNativeFeedback.SelectableBackground()}
                >
                  <View style={{
                    flex: 1,
                    height: 52,
                    justifyContent: 'center',
                    alignItems: 'center',
                    backgroundColor: '#FF5722',
                    borderRadius: 3
                  }}>
                    <Text style={{ color: '#fff', fontSize: 20 }}>Bayar</Text>
                  </View>
                </TouchableNativeFeedback>
              </View>
            </View>
          </ScrollView>


          <PopupDialog
            ref={(popupDialog) => { this.popupDialog = popupDialog; }}
            width={width * .85}
            height={height * .40}
            dialogTitle={<DialogTitle title="Pilih Bank" titleAlign="left" />} >

            <ListView
              style={{ marginBottom: 10 }}
              enableEmptySections={true}
              dataSource={this.props.bankList}
              renderRow={this._renderPopRow.bind(this)}
            />


          </PopupDialog>
        </View>
        )
          }
          }

          let {height, width} = Dimensions.get('window');


        const styles = StyleSheet.create({
        header: {
          height: 60,
          backgroundColor: '#42B549',
          flexDirection: 'row',
          justifyContent: 'flex-end'
        },
        mainContainers: {
          flex: 1,
          alignItems: 'stretch',
          backgroundColor:'#F4F4F4'
        },
        containers: {
          flex: 1,
          alignItems: 'stretch',
          margin: 20,
        },
        paymentMethod:{
          borderRadius:3,
          width:170,
          height:52,
          borderWidth:1,
          borderColor:'#F0F0F0',
          alignItems:'center',
          justifyContent:'center',
        },
        paymentSelected:{
          borderColor:'#42b549'
        },
        row: {
          backgroundColor: '#FFFFFF',
          flexDirection: 'row',
          justifyContent: 'space-between',
          borderBottomWidth: 1,
          borderColor: '#F0F0F0',
        },
        row1:{
          padding:15,
        },
        row2:{
          paddingRight:15,
          borderBottomWidth:0,
          justifyContent:'flex-end',
          paddingTop:7,
          height:30
        },
        row3:{
          paddingLeft:15,
          borderBottomWidth:0,
          paddingTop:0,
          height:24
        },
        row2Text: {
          fontSize: 10
        },
        row4: {
          flexDirection:'row',
          justifyContent:'space-between',
          padding:10,
          borderBottomWidth:0,
          paddingTop:10,
          minHeight: 60,
          flexWrap: 'wrap',
        },
        selectedBankRow: {
          flexDirection:'row',
          justifyContent:'space-between',
          padding:10,
          borderBottomWidth:0,
          paddingTop:0,
          minHeight: 60,
          flexWrap: 'wrap',
        },
        row5: {
          height:60,
          flexDirection:'row',
          justifyContent:'space-between',
          padding:10,
          paddingTop:10,
          flexWrap: 'wrap'

        },
        cardLogo: {
          flex:1,
          resizeMode: 'contain',
          height: width * .1,
          width: width * .1
        },
        buttonContainer: {
          marginTop: 20,
          borderRadius: 5
        },
        row7: {
          height: 50,
          flexDirection:'row', justifyContent:'center'
        },
        logoBox: {
          borderWidth:1,
          borderRadius:2,
          width: (width-(60+(10*6)))/6,
          marginTop: height * .015,
          justifyContent: 'center',
          borderColor:'#F0F0F0',
          alignItems: 'center',
          marginHorizontal:5
        },
        emiBox: {
          borderWidth:1,
          borderRadius:2,
          //width: width *.13,
          width: (width-(60+(10*6)))/6,
          justifyContent: 'center',
          borderColor:'#F0F0F0',
        },
        emiText : {
          fontSize: 16,
          padding: 16,
          color: '#000000b3',
          textAlign:'center'
        },
        noBorder : {
          borderWidth:0,
          borderRadius:0,
          marginTop: 0,
          height:0
        },
        disabledText: {
          backgroundColor: '#E9E9E9'
        },
        selectedBorder: {
          borderColor: '#42B549'
        },
        popupRow: {
          justifyContent: 'flex-start',
          flexDirection: 'row',
          flex: 0.1
        },
        popupCol : {
          marginLeft: width*.03,
          marginTop: width*.03
        },
        popupImage : {
          width: 100,
          height: 30
        },
        popupText : {
          marginTop: width*.015, fontSize:15
        },
        fon13 : {
          fontSize: 13
        },
        fon14 : {
          fontSize: 14
        },
        fon16 : {
          fontSize: 16
        },
        font20 : {
          fontSize: 20
        },
        font20Bold : {
          fontSize: 20,
          fontWeight: '600'
        },
        font18Bold : {
          fontSize: 18,
          fontWeight: 'bold'
        },
        fontColor70: {
          color: '#000000b3'
        },
        fontColor54: {
          color: '#0000008a'
        },
        fontColorcc: {
          color: '#000000cc'
        },
        });

const ds = new ListView.DataSource({
          rowHasChanged: (r1, r2) => r1 !== r2 });

const mapStateToProps = state => {
  const bankList = ds.cloneWithRows(state.payment.items);
  //state.payment.emiList
  // console.log(state)

  return {
    ...state.payment,
    bankList,
    paymentRate_CreditCardInterestRate: state.paymentRate.CreditCardInterestRate,
    paymentRate_CreditCardFee: state.paymentRate.CreditCardFee,
    paymentRate_CreditCardFlag: state.paymentRate.CreditCardFlag,
    paymentRate_InstallmentInterestRate: state.paymentRate.InstallmentInterestRate,
    paymentRate_InstallmentFee: state.paymentRate.InstallmentFee,
    paymentRate_InstallmentFlag: state.paymentRate.InstallmentFlag,
    paymentRate_errorMessage: state.paymentRate.errorMessage,
    paymentRate_isFetching: state.paymentRate.isFetching
  }
}

export default connect(mapStateToProps)(PaymentBank)