import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text,TextInput, View, KeyboardAvoidingView, Button, Image, ScrollView, Picker, Dimensions} from 'react-native';
import {selectPaymentOptions} from '../actions/index';
import { ccFormat, getCardType } from '../lib/utility'

class payment extends Component {

  constructor (props) {

    super (props);

    this.state = {
     months : [1, 2, 3, 4, 5, 6,7, 8, 9, 10, 11, 12],
     years: () => {
        let arr = []; 
        let d = new Date();
        let n = d.getFullYear();
        for (let i = 0; i<=10; i++) { 
          arr.push(i+n);
        }
        return arr;
      },
      errorMessage : {
        ccNum: '',
        cvv: '',
        date: ''
      }
    };
  }


  _handleButtonPress = () => {

    const errorMessage = {
        ccNum: '',
        cvv: '',
        date: ''
      };

    if (!this.props.ccNum ||
      !this.props.ccNum.trim() ||
      this.props.ccNum.length < 19 ||
      getCardType(this.props.ccNum) === "") {
      errorMessage.ccNum = 'Nomor kartu kredit tidak valid'
    }

    if (!this.props.mon || !this.props.year)  {
      errorMessage.date = 'Masukan tanggal'
    }

    if (!this.props.cvv || this.props.cvv.length < 3)  {
      errorMessage.cvv = 'CVV tidak valid'
    }

    this.setState({
      errorMessage
    });
  };

  _cardType = () => {
    const cardType = getCardType(this.props.ccNum);
    switch(cardType) {
      case 'MASTER':
        return ( <Image source={require('./img/Logo-MasterCard.png')}
                style={styles.cardLogo} />);
      case 'VISA':
        return ( <Image source={require('./img/Logo-Visa.png')}
                style={styles.cardLogo} />);
      case 'JCB':
        return (<Image source={require('./img/Logo-JCB.png')}
                style={styles.cardLogo} />);
    }

    return <View/>;
  };

  static navigationOptions = {
    title: 'Payment Page',
  };

  render() {
    console.log(this.state)
    let years = this.state.years().map( (i) => {
        return <Picker.Item key={i} value={i} label={i.toString()} />
    });

  let months = this.state.months.map( (i) => {
        return <Picker.Item key={i} value={i} label={i.toString()} />
    });



    return (
      <View style={styles.mainContainers} >
        <View style={styles.header} />
        <ScrollView>

        <View style={styles.containers} >

          <View style={[styles.row, styles.row1]} >
            <Text style={styles.row1Text}>
              Total Pembayaran
            </Text>
            <Text style={styles.row1Text}>
              Rp 34.697.000
            </Text>
          </View>

          <View style={[styles.row, styles.row2]} >
            <View style={{flex:0.5, padding: 15}}>
              <Text style={styles.row2Text}>
                Kartu Kredit
              </Text>
            </View>
            <View  style={{flex:0.5, borderColor:'black',flexDirection:'row', justifyContent:'flex-end'}}>
              <Image source={require('./img/secure-guarantee.png')}
                style={styles.secureImage} />
            </View>
          </View>

          <View style={[styles.row, styles.row4]} >
           <View style={{flex:0.5, paddingLeft: 15}}>
              <Text style={styles.row4Text}>
                Nomor kartu kredit
              </Text>
            </View>
          </View>

          <View style={[styles.row, styles.row5]} >
           <View style={{flex:0.5, paddingLeft: 10}}>
              <TextInput
                style={{fontSize:17}}
                placeholder="Contoh : 1234567890"
                placeholderTextColor = "#C6C6C6"
                keyboardType= "numeric"
                value = {ccFormat(this.props.ccNum)}
                maxLength= {19}
                onChangeText={(text) => this.props.dispatch(selectPaymentOptions('ccNum', text))}
              />
            </View>

            <View  style={{flex:0.4, borderColor:'black', flexDirection:'row', justifyContent:'center'}}>{this._cardType()}</View>
          </View>

          <View style={[styles.row, styles.row6]} >
          <View style={{flex:0.5, paddingLeft: 15}}>
              <Text style={styles.row4Text}>
                Masukan nomor kartu kredit
              </Text>
              <Text style={styles.row4Text, styles.errorText}>
               {this.state.errorMessage.ccNum}
              </Text>
            </View>
          </View>

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
               
          </View>
          <View style={styles.buttonContainer}>
            <Button
              title="Bayar"
              color='#FF5722'
              onPress={this._handleButtonPress}
            />
          </View>
        </View>
        </ScrollView>
      </View>
    )
  }
}

let {height, width} = Dimensions.get('window');

const styles = StyleSheet.create({
  header: {
    height: 60,
    backgroundColor: '#42B549',
    flexDirection: 'row'
  },
  mainContainers: {
    flex: 1,
    alignItems: 'stretch',
    backgroundColor:'#F4F4F4'
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
  row1:{
    padding:15,
  },
  row1Text : {
    fontSize: 17,
    fontWeight: 'bold',
    color: '#363636',
  },
  row2: {
    height: 80,
    padding: 0,
    borderBottomWidth:0
  },
  row3: {
    height: 70
  },
  row4: {
    height:15,
    borderBottomWidth:0
  },
  row5: {
    height:40
  },
  row6: {
    borderBottomWidth:0,
    height:40
  },
  row7: {
    height: 50,
    flexDirection:'row', justifyContent:'center'
  },
  row8: {
    height: 25,
    borderBottomWidth:0,
    flexDirection:'row', justifyContent:'center'
  },
  row9: {
    height: 30,
  },
  row7: {
    height: 50,
    flexDirection:'row', justifyContent:'center'
  },
  buttonContainer: {
    marginTop: 20,
    borderRadius: 5
  },
  secureImage: {
    flex:0.6,
    resizeMode: 'contain'
  },
  row4Text : {
    fontSize:13,
    color: '#C6C6C6'
  },
  cardLogo: {
    flex:1,
    resizeMode: 'contain',
    height:45,
    width:45
  },
  cvvLogo: {
    flex:0.5,
    resizeMode: 'contain',
    height:45,
    width:45,
  },
  errorText : {
    color: '#D50000'
  }
})

const mapStateToProps = state => {
  return {
    ...state.payment,
  }
}

export default connect(mapStateToProps)(payment)