import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text,TextInput, View, KeyboardAvoidingView, Button, Image, ScrollView, Picker,
  Dimensions, TouchableOpacity, TouchableWithoutFeedback, TouchableNativeFeedback, ListView} from 'react-native';
import PopupDialog, {DialogTitle} from 'react-native-popup-dialog';
import { StackNavigator } from 'react-navigation';
import {getBankList, selectBank, getEmiList, selectEmi } from '../actions/index';


class PaymentBank extends Component {

  constructor (props) {

    super (props);

    this.state = {
      selectedBank : null,
      otherBank: null,
      paymentMethod: null,
    };
  }

  componentDidMount() {
    this.props.dispatch(getBankList());
    this.props.dispatch(getEmiList());
  }

  _handleButtonPress = () => {

  };

  _renderPopRow(rowData: string, sectionID: number, rowID: number) {
    if(rowData.id > 9){
      return (
          <TouchableWithoutFeedback onPress={this._onPressPopupRow.bind(this, rowID, rowData)}>
            <View style={styles.popupRow}>
              <View style={styles.popupCol}>
                <Image  style={styles.popupImage} source={rowData.bankImage}/>
              </View>
              <View style={styles.popupCol}>
                <Text style={styles.popupText} > { rowData.bankName } </Text>
              </View>
            </View>
          </TouchableWithoutFeedback>
      );
    }

    return null;
  }


  _renderEmiRow(rowData: string, sectionID: number, rowID: number) {

    if (rowData.available === true) {
      return (
          <TouchableWithoutFeedback onPress={this._onPressEmiRow.bind(this, rowID, rowData)}>
            <View style={[styles.emiBox,{ marginRight:10,height:80 },rowData.isSelected ? styles.selectedBorder : {}]}>
              <Text style={styles.emiText} >
               {rowData.text}
              </Text>
            </View>
          </TouchableWithoutFeedback>
      );
    }

    return (
        <View style={[styles.emiBox, styles.disabledText]}>
          <Text style={styles.emiText} >
           {rowData.text}
          </Text>
        </View>
      );

  }

  _renderBankLogo(rowData: string, sectionID: number, rowID: number) {
    if (rowData.id < 10) {
      return (
          <TouchableWithoutFeedback onPress={this._onPressLogo.bind(this, rowID, rowData)}>
            <View style={[styles.logoBox,{ marginTop:10,height:80 },(rowData.isSelected ? styles.selectedBorder : '')]}>
              <Image source={rowData.bankImage}
                     style={styles.cardLogo} />
            </View>
          </TouchableWithoutFeedback>
      );
    } else if (rowData.id < 11) {
      return (
          <TouchableOpacity style={[styles.logoBox,{marginTop:10, height: 80}]} onPress={() => {this.popupDialog.show()} } >
            <Text style={{fontSize:10}} >
              Bank
            </Text>
            <Text style={{fontSize:10}}>
              Lainnya
            </Text>
          </TouchableOpacity>
      );
    } else if (rowData.id < 99) {
       return (<View style={[styles.logoBox, styles.noBorder]}>
    </View>);
    }

    return (<View style={{height:0}} />);
  }

  _onPressPopupRow(rowID, rowData) {
   this.setState({
     selectedBank: rowData.bankName,
     otherBank: rowData.bankImage
   },()=>{
     this.props.dispatch(selectBank(rowData.id));
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
      this.props.dispatch(selectBank(rowData.id));

      this.setState({
        selectedBank:rowData.bankName,
        otherBank: null
      });
    }
  }

  _onPressEmiRow(rowID, rowData) {
    if (!rowData.isSelected) {
      this.props.dispatch(selectEmi(rowData.id));
    }
  }


  static navigationOptions = {
    title: 'Bank Selection Page',
  };

  _choosePaymentMethod(paymentMethod){
    this.setState({
      paymentMethod
    })
  }

  render() {
    return (
        <View style={styles.mainContainers} >
          <View style={styles.header}>
            <Button
                title="Go to Payment page ->"
                color="#42B549"
                onPress={() =>
                    this.props.navigation.navigate('Payment', {})
                }
            />
            </View>
            <ScrollView>
              <View style={styles.containers} >
                <View style={[styles.row, styles.row1]} >
                  <Text>
                    Total Pembayaran
                  </Text>
                  <Text style={styles.row1Text}>
                    Rp 34.697.000
                  </Text>
                </View>

                <View style={[styles.row, styles.row2]} >
                  <Text style={styles.row2Text}>
                    Minimum Purchase Rp 500.000
                  </Text>
                </View>

                <View style={[styles.row, styles.row3]} >
                  <Text style={styles.row1Text}>
                    Pilih Bank
                  </Text>
                </View>

                <ListView
                    contentContainerStyle={[{flexDirection:'row',backgroundColor:'#fff',borderBottomWidth:0,flexWrap:'wrap',paddingLeft:10,paddingTop:10,paddingBottom:10}]}
                    dataSource = { this.props.bankList }
                    enableEmptySections={true}
                    initialListSize = {12}
                    renderRow = {this._renderBankLogo.bind(this)} />
                {
                  this.state.otherBank ?
                  (
                    <View style={{backgroundColor:'#fff',paddingHorizontal:10}}>
                      <View style={[styles.logoBox,{ marginTop:10,height:80 }, styles.selectedBorder ]}>
                        <Image source={this.state.otherBank} style={styles.cardLogo} />
                      </View>
                    </View>
                  ) :
                  null
                }

                {
                  (this.state.selectedBank === null) ? null :
                  (
                    <View style={{alignItems:'flex-end',paddingHorizontal:15,backgroundColor:'#fff'}}>
                      <Text>
                        Anda memilih {this.state.selectedBank}
                      </Text>
                    </View>
                  )
                }


                <View style={[styles.row, styles.row3]} >
                  <Text style={styles.row1Text}>
                    Cicilan Kartu Kredit Bunga 0%
                  </Text>
                </View>

                <ListView
                    contentContainerStyle={{paddingTop:20,flexDirection:'row',backgroundColor:'#fff',paddingLeft:15}}
                    dataSource = { this.props.emiList }
                    enableEmptySections={true}
                    renderRow = {this._renderEmiRow.bind(this)} />

                <View style={{paddingTop:10,backgroundColor:'white',borderBottomWidth:1,borderColor:'#F0F0F0'}}>
                  <Text style={[styles.row1Text,{marginLeft:15}]}>
                    Pilih Metode Pembayaran
                  </Text>
                  <View style={{paddingVertical:20,flexDirection:'row',marginLeft:15}}>
                    <TouchableWithoutFeedback onPress={this._choosePaymentMethod.bind(this,'SCAN')}>
                      <View style={[styles.paymentMethod,{marginRight:10},(this.state.paymentMethod === 'SCAN')?styles.paymentSelected:{}]}>
                        <Text style={styles.emiText}>
                          Scan Kartu Kredit
                        </Text>
                      </View>
                    </TouchableWithoutFeedback>

                    <TouchableWithoutFeedback onPress={this._choosePaymentMethod.bind(this,'ONLINE')}>
                      <View style={[styles.paymentMethod,(this.state.paymentMethod === 'ONLINE')?styles.paymentSelected:{}]}>
                        <Text style={styles.emiText}>
                          Pembayaran Online
                        </Text>
                      </View>
                    </TouchableWithoutFeedback>
                  </View>
                </View>

                <View style={[styles.row, styles.row1]} >
                  <Text>
                    Nominal
                  </Text>
                  <Text style={styles.row1Text}>
                    Rp 2.266.334/bulan
                  </Text>
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
                        flex:1,
                        height:52,
                        justifyContent:'center',
                        alignItems:'center',
                        backgroundColor:'#FF5722',
                        borderRadius:3
                      }}>
                        <Text style={{color:'#fff',fontSize:16}}>
                          Bayar
                        </Text>
                      </View>
                  </TouchableNativeFeedback>
                </View>
              </View>
            </ScrollView>


            <PopupDialog
                ref={(popupDialog) => { this.popupDialog = popupDialog; }}
                width = {width*.85}
                height= {height*.40}
                dialogTitle={<DialogTitle title="Pilih Bank" titleAlign="left"/>} >

              <ListView
                  style={{marginBottom: 10}}
                  enableEmptySections={true}
                  dataSource = { this.props.bankList }
                  renderRow = {this._renderPopRow.bind(this)}
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
          fontSize:10,
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
          width: width *.12,
          height: width*.05
        },
          popupText : {
          marginTop: width*.015, fontSize:15
        }
        });

const ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2 });

const mapStateToProps = state => {
  const bankList = ds.cloneWithRows(state.payment.items);
  const emiList = ds.cloneWithRows(state.payment.emiList);

  return {
     ...state.payment,
    bankList,
    emiList
  }
}

export default connect(mapStateToProps)(PaymentBank)
