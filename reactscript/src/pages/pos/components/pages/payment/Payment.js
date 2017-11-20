import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, TextInput, TouchableNativeFeedback, View, KeyboardAvoidingView, Button, Image, ScrollView, Picker, Dimensions } from 'react-native'
import { selectPaymentOptions, makePayment } from '../../../actions/index'
import { ccFormat, getCardType } from '../../../lib/utility'
import { NavigationModule } from 'NativeModules'
import { Text } from '../../../common/TKPText'
import { icons } from '../../../lib/config'
import numeral from 'numeral'



class payment extends Component {
  constructor(props) {
    super(props);

    this.state = {
      months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      years: () => {
        let arr = [];
        let d = new Date();
        let n = d.getFullYear();
        for (let i = 0; i <= 10; i++) {
          arr.push(i + n);
        }
        return arr;
      },
      errorMessage: {
        ccNum: '',
        cvv: '',
        date: ''
      }
    };
  }

  _handleButtonPress = () => {
    const { 
      selectedBankData, 
      selectBank, 
      selectIdBank, 
      selectedEmiId,
      checkout_data,
      ccNum,
      mon,
      year,
      cvv,
    } = this.props

    const errorMessage = {
      ccNum: '',
      cvv: '',
      date: ''
    };

    // Validation 1 temporary
    // if (!ccNum || !ccNum.trim() || ccNum.length < 19 || getCardType(ccNum) === "") {
    //   errorMessage.ccNum = 'Nomor kartu kredit tidak valid'
    // }
    
    // Validation 2
    // start of oka
    if(selectedEmiId === 0) {
      if(!selectedBankData.validate_bin.find((value) => {
        return ccNum.replace(' ', '').startsWith(value)
      }))  {
        errorMessage.ccNum = 'Nomor kartu kredit tidak sesuai dengan bank yang dipilih ' + selectBank
      }
    } else {
      if(!selectedBankData.installment_bin.find((value) => {
        return ccNum.replace(' ', '').startsWith(value)
      }))  {
        errorMessage.ccNum = 'Nomor kartu kredit tidak boleh mengambil cicilan';
      }
    }
    // end of oka

    // Validation 3
    if (!mon || !year) {
      errorMessage.date = 'Masukan tanggal'
    }

    // Validation 4
    if (!cvv || cvv.length < 3) {
      errorMessage.cvv = 'CVV tidak valid'
    }

    this.setState({ errorMessage })
    
    // Validation 5
    if (errorMessage.ccNum == '' && errorMessage.cvv == '' && errorMessage.date == '') {
      const json_checkout_data = JSON.parse(checkout_data)
      const data_process = {
        checkout_data: json_checkout_data,
        selectedEmiId: selectedEmiId,
        selectedBankData: selectedBankData,
        ccNum: this.props.ccNum,
        mon: this.props.mon,
        year: this.props.year,
        cvv: this.props.cvv
      }
      
      NavigationModule.navigate('posapp://payment/process', JSON.stringify(data_process))
    }
  }



  _cardType = () => {
    const cardType = getCardType(this.props.ccNum)

    switch (cardType) {
      case 'MASTER':
      return ( <Image source={{ uri: icons.mastercard }}
          style={styles.cardLogo} resizeMode={'contain'} />)
      case 'VISA':
      return ( <Image source={{ uri: icons.visa }}
          style={styles.cardLogo} resizeMode={'contain'} />)
      case 'JCB':
      return (<Image source={{ uri: icons.jcb }}
          style={styles.cardLogo} resizeMode={'contain'} />)
    }
  }



  static navigationOptions = {
    title: 'Pembayaran',
    headerTintColor: '#FFF',
    headerStyle: {
        backgroundColor: '#42B549'
    }
    // headerMode: 'none'
  }

  render() {
    const { dataPaymentBank, total_payment_with_rates_or_fees } = this.props
    
    const checkout_data = JSON.parse(dataPaymentBank.checkout_data)
    const total_payment = (dataPaymentBank.total_payment).toLocaleString("id")
    // console.log(total_payment, typeof total_payment)
    const total_payment_with_rates_or_fees_with_currency = total_payment_with_rates_or_fees == 0 ? total_payment : numeral(total_payment_with_rates_or_fees).format('0,0') 


    
    let years = this.state.years().map((i) => {
      return <Picker.Item key={i} value={i} label={i.toString()} />
    })

    let months = this.state.months.map((i) => {
      return <Picker.Item key={i} value={i} label={i.toString()} />
    })

    return (
      <View style={styles.mainContainers} >
        {/* <View style={styles.header} /> */}
        <ScrollView>

          <View style={styles.containers} >

            <View style={[styles.row, styles.row1]} >
              <Text style={styles.row1Text}>Total Pembayaran</Text>
              <Text style={styles.row1Text}>Rp {total_payment_with_rates_or_fees_with_currency}</Text>
            </View>

            <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', backgroundColor: '#fff', paddingBottom: 0, paddingHorizontal: 20 }} >
              <View style={{ flex: 0.5 }}>
                <Text style={styles.row2Text}>Kartu Kredit</Text>
              </View>
              <View style={{ flex: 0.5, alignItems: 'flex-end' }}>
                <Image source={{ uri: icons.secure_guarantee }} style={{width:75,height:55}} resizeMode={'contain'}/>
              </View>
            </View>

            <View style={{ flex: 1, flexDirection: 'row', backgroundColor: '#fff', paddingBottom: 0, paddingHorizontal: 20 }} >
              <View style={{ flex: 0.5, justifyContent: 'flex-end', borderBottomWidth: 2, borderBottomColor: '#F0F0F0' }}>
                <Text style={[styles.row4Text, {marginBottom: 8}]}>Nomor kartu kredit</Text>
                <TextInput
                  underlineColorAndroid={'transparent'}
                  style={{ fontSize: 16, paddingLeft: 0, paddingVertical: 0 }}
                  placeholder="Contoh : 1234567890"
                  placeholderTextColor="#00000061"
                  keyboardType="numeric"
                  value={ccFormat(this.props.ccNum)}
                  maxLength={19}
                  onChangeText={(text) => this.props.dispatch(selectPaymentOptions('ccNum', text))}
                />
              </View>
              <View style={{ flex: 0.5, height: 60, flexDirection: 'row', alignItems: 'flex-end', justifyContent: 'flex-end', borderBottomWidth: 2, borderBottomColor: '#F0F0F0' }}>
                {
                  (getCardType(this.props.ccNum) === 'MASTER') ?
                  <Image source={{ uri: icons.mastercard }} style={{width:60,height:60,flex:0.2}} resizeMode={'contain'} />
                    :
                    (getCardType(this.props.ccNum) === 'VISA') ?
                    <Image source={{ uri: icons.visa }} style={{width:60,height:60,flex:0.2}}  resizeMode={'contain'} />
                      :
                      (getCardType(this.props.ccNum) === 'JCB') ?
                      <Image source={{ uri: icons.jcb }} style={{width:60,height:60,flex:0.2}}  resizeMode={'contain'} />
                        :
                        (
                          <View style={{ flexDirection: 'row', marginRight: -10 }}>
                            <Image source={{ uri: icons.mastercard }} style={styles.cardLogo} resizeMode={'contain'} />
                            <Image source={{ uri: icons.visa }} style={styles.cardLogo}  resizeMode={'contain'} />
                            <Image source={{ uri: icons.jcb }} style={styles.cardLogo}  resizeMode={'contain'} />
                          </View>
                        )
                }
              </View>
              {/*<View style={{flex:1, paddingLeft: 10}}>
              <TextInput
                underlineColorAndroid={'transparent'}
                style={{fontSize:17}}
                placeholder="Contoh : 1234567890"
                placeholderTextColor = "#C6C6C6"
                keyboardType= "numeric"
                value = {ccFormat(this.props.ccNum)}
                maxLength= {19}
                onChangeText={(text) => this.props.dispatch(selectPaymentOptions('ccNum', text))}
              />
            </View>
            <View  style={{flex:1, borderColor:'black', flexDirection:'row', justifyContent:'center'}}>
              <Image source={require('./img/Logo-MasterCard.png')} style={styles.cardLogo} />
              <Image source={require('./img/Logo-JCB.png')}  style={styles.cardLogo} />
              <Image source={require('./img/Logo-Visa.png')}  style={styles.cardLogo} />
            </View>*/}
            </View>

            <View style={[styles.row, styles.row6]} >
              <View style={{ flex: 0.5, paddingLeft: 20 }}>
                <Text style={[styles.row4Text, styles.errorText]}>
               {this.state.errorMessage.ccNum}
              </Text>
              <Text style={[styles.row4Text, {color: '#00000061'}]}>Masukan nomor kartu kredit</Text>
            </View>
          </View>

          <View style={{ backgroundColor: '#fff', flexDirection: 'row', paddingTop: 25 }}>
            <View style={{ flex: 0.4, marginLeft: 20 }}>
              <Text style={styles.row4Text}>Masa Berlaku</Text>
              <View style={{ flexDirection: 'row', marginLeft: -3 }}>
                <View style={{ borderBottomWidth: 2.5, borderBottomColor: '#F0F0F0', marginRight: 20 }}>
                  <Picker style={{ width: width * .15 }}
                    selectedValue={this.props.mon}
                    onValueChange={(itemValue, itemIndex) => this.props.dispatch(selectPaymentOptions('mon', itemValue))}>
                    <Picker.Item label="mm" value="" />
                    {months}
                  </Picker>
                </View>
                <View style={{ borderBottomWidth: 2.5, borderBottomColor: '#F0F0F0' }}>
                  <Picker style={{ width: width * .15 }}
                    selectedValue={this.props.year}
                    onValueChange={(itemValue, itemIndex) => this.props.dispatch(selectPaymentOptions('year', itemValue))}>
                    <Picker.Item label="YY" value="" />
                    {years}
                  </Picker>
                </View>
              </View>
              <Text style={styles.errorText}>
                {this.state.errorMessage.date}
              </Text>
            </View>
            <View style={{ flex: 0.275, marginRight: 15, borderBottomWidth: 2, borderBottomColor: '#F0F0F0', marginBottom: 1 }}>
              <Text style={styles.row4Text}>CVV</Text>
              <View style={{ flex: 1, backgroundColor: 'white', flexDirection: 'row', alignItems: 'center' }}>
                <TextInput
                  underlineColorAndroid={'transparent'}
                  style={{ fontSize: 16, width: 150, marginBottom: -2, paddingBottom: 0, paddingLeft: 0 }}
                  placeholder="Contoh : 123"
                  secureTextEntry={true}
                  placeholderTextColor="#00000061"
                  keyboardType="numeric"
                  maxLength={3}
                  onChangeText={(text) => this.props.dispatch(selectPaymentOptions('cvv', text))}
                />
                <View style={{ flex: 1, marginBottom: -5 }}>
                <Image source={{ uri: icons.cvv }} style={styles.cvvLogo} resizeMode={'contain'}/>
                </View>
              </View>
            </View>
          </View>

          <View style={{ flexDirection: 'row', backgroundColor: 'white', paddingHorizontal: 20, paddingBottom: 25 }}>
            <View style={{ flex: 0.4 }} />
            <View style={{ flex: 0.27 }}>
              <Text style={styles.errorText}>
                {this.state.errorMessage.cvv}
              </Text>
              <Text style={[styles.row4Text, {color: '#00000061'}]}>Masukan CVV</Text>
            </View>
          </View>
          {/*
          <View style={[styles.row, styles.row7]} >
            <View style={{flex:0.6, flexDirection:'row',}}>
              <Picker  style={{width:width*.15, marginLeft: 5}}
                selectedValue={this.props.mon}
                onValueChange={(itemValue, itemIndex) => this.props.dispatch(selectPaymentOptions('mon', itemValue))}>
                <Picker.Item label="mm" value="" />
                {months}
              </Picker>
              <Picker  style={{width:width*.15}}
                selectedValue={this.props.year}
                  onValueChange={(itemValue, itemIndex) => this.props.dispatch(selectPaymentOptions('year', itemValue))}>
                <Picker.Item label="YY" value="" />
                {years}
              </Picker>
            </View>
            <View style={{flex:0.5, flexDirection:'row'}}>
              <TextInput
                underlineColorAndroid={'transparent'}
                style={{width:100, height:50, fontSize:17}}
                placeholder="Contoh : 123"
                secureTextEntry={true}
                placeholderTextColor = "#C6C6C6"
                keyboardType= "numeric"
                maxLength= {3}
                onChangeText={(text) => this.props.dispatch(selectPaymentOptions('cvv', text))}
              />
              <Image source={require('./img/cvv-icon.png')}
                style={styles.cvvLogo} />
            </View>
          </View>
          <View style={[styles.row, styles.row8]} >
            <View style={{flex:0.6, flexDirection:'row',paddingLeft: 15}}>
              <Text style={styles.row4Text, styles.errorText}>
               {this.state.errorMessage.date}
              </Text>
            </View>
             <View style={{flex:0.5, flexDirection:'row'}}>
              <Text style={styles.row4Text}>
                Masukan CVV
              </Text>
            </View>
          </View>
            <View style={[styles.row, styles.row8, styles.row9]} >
             <View style={{flex:0.6, flexDirection:'row',paddingLeft: 15}}>
            </View>
             <View style={{flex:0.5, flexDirection:'row'}}>
              <Text style={styles.row4Text, styles.errorText}>
               {this.state.errorMessage.cvv}
              </Text>
            </View>
          </View>*/}

          <View style={styles.buttonContainer}>
            {/*<Button
              title="Bayar"
              color='#FF5722'
              onPress={this._handleButtonPress}
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
      </View >
    )
  }
}



let { height, width } = Dimensions.get('window');

const styles = StyleSheet.create({
  header: {
    height: 60,
    backgroundColor: '#42B549',
    flexDirection: 'row'
  },
  mainContainers: {
    flex: 1,
    alignItems: 'stretch',
    backgroundColor: '#F4F4F4'
  },
  containers: {
    flex: 2,
    alignItems: 'stretch',
    margin: 20,
  },
  row: {
    height: 60,
    backgroundColor: '#FFFFFF',
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderBottomWidth: 1,
    borderColor: '#F0F0F0'
  },
  row1: {
    padding: 15,
  },
  row1Text: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#000000b3',
  },
  row2Text: {
    fontSize: 20,
    color: '#000000b3'
  },
  row2: {
    height: 80,
    padding: 0,
    borderBottomWidth: 0
  },
  row3: {
    height: 70
  },
  row4: {
    height: 15,
    borderBottomWidth: 0
  },
  row5: {
    height: 40
  },
  row6: {
    borderBottomWidth: 0,
    height: 40
  },
  row7: {
    height: 50,
    flexDirection: 'row', justifyContent: 'center'
  },
  row8: {
    height: 25,
    borderBottomWidth: 0,
    flexDirection: 'row', justifyContent: 'center'
  },
  row9: {
    height: 30,
  },
  row7: {
    height: 50, flexDirection: 'row', justifyContent: 'center'
  },
  buttonContainer: {
    marginTop: 20,
    borderRadius: 5
  },
  secureImage: {
    flex: 0.6
  },
  row4Text: {
    fontSize: 16,
    color: '#000000b3'
  },
  cardLogo: {
    height: 55,
    width: 55
  },
  cvvLogo: {
    flex: 0.5,
    resizeMode: 'contain',
    height: 45,
    width: 45,
  },
  errorText: {
    color: '#D50000'
  }
})

const mapStateToProps = (state, ownProps) => {
  // console.log(ownProps)
  return {
    ...state.payment,
    selectedBankData: ownProps.navigation.state.params.selectedBankData,
    selectBank: ownProps.navigation.state.params.selectBank,
    selectIdBank: ownProps.navigation.state.params.selectIdBank,
    selectedEmiId: ownProps.navigation.state.params.selectedEmiId,
    checkout_data: ownProps.navigation.state.params.checkout_data,
    dataPaymentBank: ownProps.navigation.state.params,
    total_payment_with_rates_or_fees: ownProps.navigation.state.params.total_payment_with_rates_or_fees,
  }
}

export default connect(mapStateToProps)(payment)