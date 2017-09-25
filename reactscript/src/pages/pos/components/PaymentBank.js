import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  StyleSheet, TextInput, View, KeyboardAvoidingView, Button, Image, ScrollView, Picker,
  Dimensions, TouchableOpacity, TouchableWithoutFeedback, TouchableNativeFeedback, ListView
} from 'react-native';
import PopupDialog, { DialogTitle } from 'react-native-popup-dialog';
import { StackNavigator } from 'react-navigation';
import { getBankList, selectBank, getEmiList, selectEmi } from '../actions/index';
import { Text } from '../common/TKPText'
import { NavigationModule } from 'NativeModules'

class PaymentBank extends Component {

  constructor(props) {

    super(props);

    const dsEmi = new ListView.DataSource({
          rowHasChanged: (r1, r2) => r1 !== r2 });

    this.state = {
      selectedBank: null,
      selectIdBank: null,
      selectedEmiId: null,
      otherBank: null,
      paymentMethod: null,
      emiList: dsEmi.cloneWithRows([{
        term: 0,
        text: "Tanpa Cicilan",
        available: false
      }]),
      dsEmi: dsEmi
    };
  }

  componentDidMount() {
    console.log(this.props)
    console.log(this.props.screenProps.total_payment)
    this.props.dispatch(getBankList());
  }

  _handleButtonPress = () => {
    const selected_installment = !this.state.selectedEmiId ? 0 : (this.state.selectedEmiId)
    if (this.state.paymentMethod === 'SCAN'){
      NavigationModule.navigate("posapp://payment/scan/3/6", "")
    } else {
      // console.log(this.state.selectedBank, this.state.selectIdBank, this.state.selectedEmiId)
      this.props.navigation.navigate('Payment', {
        selectBank: this.state.selectedBank,
        selectIdBank: this.state.selectIdBank,
        selectedEmiId: selected_installment,
        total_payment: this.props.screenProps.total_payment
      })
    }
  };

  _renderPopRow(rowData: string, sectionID: number, rowID: number) {
    if (rowID > 10) {
      return (
        <TouchableWithoutFeedback onPress={this._onPressPopupRow.bind(this, rowID, rowData)}>
          <View style={styles.popupRow}>
            <View style={styles.popupCol}>
              <Image style={styles.popupImage} source={{uri: rowData.bank_logo}} />
            </View>
            <View style={styles.popupCol}>
              <Text style={styles.popupText} > {rowData.bank_name} </Text>
            </View>
          </View>
        </TouchableWithoutFeedback>
      );
    }

    return (null);
  }


  _renderEmiRow(rowData: string, sectionID: number, rowID: number) {

    if (rowData.available === false) {
      return (
      <TouchableWithoutFeedback onPress={this._onPressEmiRow.bind(this, rowID, rowData)}>
        <View style={[styles.emiBox, { marginRight: 10, height: 80 }, this.state.selectedEmiId === rowData.term ? styles.selectedBorder : {}]}>
          <Text style={styles.emiText} >
              {rowData.text}
          </Text>
        </View>
      </TouchableWithoutFeedback>
      );
   }

   return (
      <TouchableWithoutFeedback onPress={this._onPressEmiRow.bind(this, rowID, rowData)}>
        <View style={[styles.emiBox, { marginRight: 10, height: 80 }, this.state.selectedEmiId === rowData.term ? styles.selectedBorder : {}]}>
          <Text style={styles.emiText} >
            {rowData.term} Bulan
          </Text>
        </View>
      </TouchableWithoutFeedback>
    );
  }

  _renderBankLogo(rowData: string, sectionID: number, rowID: number) {
    if (rowID <= 10) {
      return (
        <TouchableWithoutFeedback onPress={this._onPressLogo.bind(this, rowID, rowData)}>
          <View style={[styles.logoBox, { marginTop: 10, height: 80 }, (rowData.isSelected ? styles.selectedBorder : '')]}>
            <Image source={{uri: rowData.bank_logo}}
              style={styles.cardLogo} />
          </View>
        </TouchableWithoutFeedback>
      );
    } else if (rowID <= 11) {
      return (
        <TouchableOpacity style={[styles.logoBox, { marginTop: 10, height: 80 }]} onPress={() => { this.popupDialog.show() }} >
          <Text style={{ fontSize: 13, color: '#0000008a' }} >
            Bank
            </Text>
          <Text style={{ fontSize: 13, color: '#0000008a' }}>
            Lainnya
            </Text>
        </TouchableOpacity>
      );
    } else if (rowID < 99) {
      return (<View style={[styles.logoBox, styles.noBorder]}>
      </View>);
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
      emiList: emiList
    }, () => {
      this.props.dispatch(selectBank(rowData.bank_id));
      this.popupDialog.dismiss();
    });
    /*if (rowData.id > 9) {
     this.setState({
       selectedBank: rowData.bankName,
       otherBank: rowData.bankImage
     });
    } else if (rowData.id <= 9) {
     this.setState({
       selectedBank: rowData.bankName,
       otherBank: null
     });
   }*/
  }

  _onPressLogo(rowID, rowData) {
    if (!rowData.isSelected) {
      let emiList = this.state.dsEmi.cloneWithRows([{
        text: "Tanpa Cicilan",
        available: false
      }]);
      
      if (rowData.allow_installment) {
   /*   this.props.bankList.forEach((data) => {
          if (data.bank_id == rowData.bank_id) {
            emiList = this.state.dsEmi.cloneWithRows([...[{
              text: "Tanpa Cicilan",
              available: false
            }], ...data.installment_list]);
            return
          }
        });*/

         emiList = this.state.dsEmi.cloneWithRows([...[{
              text: "Tanpa Cicilan",
              available: false
            }], ...rowData.installment_list]);
      }
      
      this.props.dispatch(selectBank(rowData.bank_id));

      this.setState({
        selectedBank: rowData.bank_name,
        selectIdBank: rowData.bank_id,
        otherBank: null,
        selectedEmiId: null,
        emiList: emiList
      });
    }
  }

  _onPressEmiRow(rowID, rowData) {
   this.setState({
    selectedEmiId: rowData.term
   });
  }


  static navigationOptions = {
    title: 'Pembayaran',
    headerTintColor: '#FFF',
    headerStyle: {
        backgroundColor: '#42B549'
    },
    headerLeft: <Image 
      source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/arrow_back.png' }} 
      style={{ width: 15, height: 15, marginLeft: 20 }} />
  };

  _choosePaymentMethod(paymentMethod) {
    this.setState({
      paymentMethod
    })
  }

  render() {
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
                <Text style={[styles.font16, styles.fontColor70]}>
                  Total Pembayaran
                  </Text>
                <Text style={[styles.font16, styles.fontColor70]}>
                  Rp {this.props.screenProps.total_payment}
                  </Text>
              </View>

              <View style={[styles.row, styles.row2]} >
                <Text style={[styles.font13, styles.fontColor70]}>
                  Minimum Purchase Rp 500.000
                  </Text>
              </View>

              <View style={[styles.row, styles.row3]} >
                <Text style={[styles.font14, styles.fontColor70]}>
                  Pilih Bank
                  </Text>
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
                      <Text>
                        Anda memilih {this.state.selectedBank}
                      </Text>
                    </View>
                  )
              }


              <View style={[styles.row, styles.row3]} >
                <Text style={[styles.font14, styles.fontColor70]}>
                  Cicilan Kartu Kredit Bunga 0%
                  </Text>
              </View>

              <ListView
                contentContainerStyle={{ paddingTop: 20, flexDirection: 'row', backgroundColor: '#fff', paddingLeft: 15 }}
                dataSource={this.state.emiList}
                enableEmptySections={true}
                renderRow={this._renderEmiRow.bind(this)} />

              <View style={{ paddingTop: 10, backgroundColor: 'white', borderBottomWidth: 1, borderColor: '#F0F0F0' }}>
                <Text style={[styles.font14, styles.fontColor70, { marginLeft: 15 }]}>
                  Pilih Metode Pembayaran
                  </Text>
                <View style={{ paddingVertical: 20, flexDirection: 'row', marginLeft: 15 }}>
                  <TouchableWithoutFeedback onPress={this._choosePaymentMethod.bind(this, 'SCAN')}>
                    <View style={[styles.paymentMethod, { marginRight: 10 }, (this.state.paymentMethod === 'SCAN') ? styles.paymentSelected : {}]}>
                      <Text style={[styles.emiText, {fontSize: 14}]}>
                        Scan Kartu Kredit
                        </Text>
                    </View>
                  </TouchableWithoutFeedback>

                  <TouchableWithoutFeedback onPress={this._choosePaymentMethod.bind(this, 'ONLINE')}>
                    <View style={[styles.paymentMethod, (this.state.paymentMethod === 'ONLINE') ? styles.paymentSelected : {}]}>
                      <Text style={[styles.emiText, {fontSize: 14}]}>
                        Pembayaran Online
                        </Text>
                    </View>
                  </TouchableWithoutFeedback>
                </View>
              </View>

              {this.props.screenProps.total_payment && this.state.selectedEmiId && 
              <View style={[styles.row, styles.row1]}>
                <Text style={[styles.font20, styles.fontColor70]}>Nominal</Text>
                <Text style={[styles.font20, styles.fontColor70]}>
                  Rp {this.props.screenProps.total_payment / this.state.selectedEmiId}/ bulan</Text>
              </View>}
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
                    <Text style={{ color: '#fff', fontSize: 16 }}>Bayar</Text>
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
          width:150,
          height:80,
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
          height:20
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
          fontSize:13,
          color: '#0000008a',
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

  return {
          ...state.payment,
        bankList
  }
}

export default connect(mapStateToProps)(PaymentBank)